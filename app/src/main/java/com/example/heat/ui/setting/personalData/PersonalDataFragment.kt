package com.example.heat.ui.setting.personalData

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.databinding.FragmentPersonalDataBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.survey.SurveyFragmentDirections
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UiUtils.Companion.getUserIDFromDataStore
import com.example.heat.util.UiUtils.Companion.isEditTextEmpty
import com.example.heat.util.UiUtils.Companion.isNetworkConnected
import com.example.heat.util.UiUtils.Companion.showSimpleSnackBar
import com.example.heat.util.UserIDManager
import com.example.heat.util.UserManager
import com.example.heat.util.enumerian.*
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory


class PersonalDataFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((UserPreferences?) -> PersonalDataViewModelFactory) by factory()

    private lateinit var viewModel: PersonalDataViewModel

    private var _binding: FragmentPersonalDataBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { PersonalDataFragmentArgs.fromBundle(it) }
        val userPref = safeArgs?.userPreferences
        val comeFrom = safeArgs?.comeFrom
        viewModel = ViewModelProvider(
            this,
            viewModelFactoryInstanceFactory(userPref)
        )[PersonalDataViewModel::class.java]

        var isFromProfile = false
        var shouldUserSurveyDir = false

        if (userPref != null) {
            setData(userPref)
        } else {
            shouldUserSurveyDir = true
        }
        if (comeFrom == ComeFrom.PROFILE) isFromProfile = true
        bindUI(isFromProfile, shouldUserSurveyDir, userPref)
    }

    private fun setData(userPreferences: UserPreferences) {
        binding.apply {
            name.setText(userPreferences.name)
            weight.setText(userPreferences.weight.toString())
            height.setText(userPreferences.height.toString())
            age.setText(userPreferences.age.toString())
            when (userPreferences.gender) {
                Gender.MALE -> toggleButtonGroupSex.check(R.id.male)
                Gender.FEMALE -> toggleButtonGroupSex.check(R.id.female)
            }
        }
    }

    private fun bindUI(
        isFromProfile: Boolean,
        shouldUserSurveyDir: Boolean,
        userPreference: UserPreferences?
    ) = launch {
        val id = getUserIDFromDataStore(requireContext(),viewLifecycleOwner)
        val userPref: UserPreferences = userPreference ?: UserPreferences(
            id,
            "",
            0.0,
            0.0,
            0,
            Gender.MALE,
            ActiveLevel.MODERATELY,
            AbstractGoal.MAINTAIN,
            UserDietType.ANY_THING,
            arrayListOf(),
            arrayListOf()
        )

        if (isFromProfile) {
            binding.navigationLayout.visibility = View.GONE
            binding.toolbarLayout.save.visibility = View.VISIBLE
            binding.toolbarLayout.backArrow.visibility = View.VISIBLE
            binding.toolbarLayout.progressView.visibility = View.INVISIBLE

        } else {
            binding.navigationLayout.visibility = View.VISIBLE
            binding.toolbarLayout.save.visibility = View.GONE
            binding.toolbarLayout.backArrow.visibility = View.GONE
            binding.toolbarLayout.progressView.visibility = View.VISIBLE
            binding.toolbarLayout.progressView.progress = 0f
            binding.toolbarLayout.progressView.labelText = "0 out of 6 completed"
        }

        binding.apply {
            if (isFromProfile) {
                toolbarLayout.save.setOnClickListener {

                    val user = saveData(binding, userPref)

                    //update user preferences
                    viewModel.updateUserPreferences(user)

                    //send updated user pref to server
                    if(isNetworkConnected(requireActivity()))
                        viewModel.updateUserPreferencesToServer(user)

                }
            } else {
                next.setOnClickListener {
                    saveData(binding, userPref)
                }
            }
            toolbarLayout.backArrow.setOnClickListener {
                saveData(binding, userPref)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is PersonalDataViewModel.PersonalDataTransactionsEvents.NavigateToActiveLevelScreen -> {
                        Log.e("USER_PREF_PERSONAL_DATA", userPreference.toString())
                        when (isFromProfile) {
                            true -> {
                                requireActivity().onBackPressed()
                            }
                            false -> {
                                if (shouldUserSurveyDir) {
                                    val actionAdd =
                                        SurveyFragmentDirections.actionSurveyFragmentToActiveLevelFragment(
                                            userPref,
                                            ComeFrom.SURVEY
                                        )
                                    findNavController().navigate(actionAdd)
                                } else {
                                    val actionAdd =
                                        PersonalDataFragmentDirections.actionPersonalDataFragmentToActiveLevelFragment(
                                            userPref,
                                            ComeFrom.SURVEY
                                        )
                                    findNavController().navigate(actionAdd)
                                }
                            }
                        }

                    }
                    else -> {
                        showSimpleSnackBar(binding.root, getString(R.string.complete_all_parts))
                    }
                }
            }
        }.exhaustive
    }

    private fun saveData(
        binding: FragmentPersonalDataBinding,
        userPreference: UserPreferences
    ):UserPreferences {
        binding.apply {

            if (!isEditTextEmpty(name) and !isEditTextEmpty(weight) and !isEditTextEmpty(height) and !isEditTextEmpty(
                    age
                )
            ) {
                userPreference.id = getUserIDFromDataStore(requireContext(),viewLifecycleOwner)
                userPreference.name = name.text.toString()
                userPreference.height = height.text.toString().toDouble()
                userPreference.weight = weight.text.toString().toDouble()
                userPreference.age = age.text.toString().toInt()
                when {
                    male.isChecked -> userPreference.gender = Gender.MALE
                    female.isChecked -> userPreference.gender = Gender.FEMALE
                    else -> userPreference.gender = Gender.MALE
                }
                viewModel.onNextClicked(userPreference)
                return userPreference
            } else {
                viewModel.shouldShowFillAllPartSnackBar()
                return userPreference
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}