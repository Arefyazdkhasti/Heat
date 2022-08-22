package com.example.heat.ui.setting.personalData

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.databinding.FragmentPersonalDataBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.survey.SurveyFragmentDirections
import com.example.heat.util.UiUtils.Companion.isEditTextEmpty
import com.example.heat.util.UiUtils.Companion.showSimpleSnackBar
import com.example.heat.util.enumerian.*
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory


//TODO Save data when slide with viewPager
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
        }else{
            shouldUserSurveyDir = true
        }
        if(comeFrom == ComeFrom.PROFILE) isFromProfile = true
        bindUI(isFromProfile,shouldUserSurveyDir)
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

    private fun bindUI(isFromProfile: Boolean, shouldUserSurveyDir: Boolean) = launch {
        if (isFromProfile) {
            binding.navigationLayout.visibility = View.GONE
            binding.save.visibility = View.VISIBLE
        } else {
            binding.navigationLayout.visibility = View.VISIBLE
            binding.save.visibility = View.GONE
            binding.backArrow.visibility = View.GONE
        }

        //TODO get userPreference from DAO
        val userPreference = UserPreferences(
            1,
            "",
            0.0,
            0.0,
            0,
            Gender.MALE,
            ActiveLevel.MODERATELY,
            AbstractGoal.MAINTAIN,
            DietType.ANY_THING,
            arrayListOf(),
            arrayListOf()
        )
        binding.apply {
            if (isFromProfile) {
                save.setOnClickListener {
                    saveData(binding, userPreference)
                }
            } else {
                next.setOnClickListener {
                    saveData(binding, userPreference)
                }
            }
            backArrow.setOnClickListener {
                saveData(binding, userPreference)
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
                                if(shouldUserSurveyDir){
                                    val actionAdd = SurveyFragmentDirections.actionSurveyFragmentToActiveLevelFragment(
                                        userPreference,
                                        ComeFrom.SURVEY
                                    )
                                    findNavController().navigate(actionAdd)
                                }else {
                                    val actionAdd =
                                        PersonalDataFragmentDirections.actionPersonalDataFragmentToActiveLevelFragment(
                                            userPreference,
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

    private fun saveData(binding: FragmentPersonalDataBinding, userPreference: UserPreferences) {
        binding.apply {

            if (!isEditTextEmpty(name) and !isEditTextEmpty(weight) and !isEditTextEmpty(height) and !isEditTextEmpty(
                    age
                )
            ) {
                userPreference.name = name.text.toString()
                userPreference.height = height.text.toString().toDouble()
                userPreference.weight = weight.text.toString().toDouble()
                userPreference.age = age.text.toString().toInt()
                when {
                    male.isChecked -> userPreference.gender = Gender.MALE
                    female.isChecked -> userPreference.gender = Gender.FEMALE
                    //TODO show toast is none is checked
                    else -> userPreference.gender = Gender.MALE
                }
                viewModel.onNextClicked(userPreference)
            } else {
                viewModel.shouldShowFillAllPartSnackBar()
            }
        }
    }
}