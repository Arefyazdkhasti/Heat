package com.example.heat.ui.setting.activeLevel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.databinding.FragmentActiveLevelBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.util.enumerian.ActiveLevel
import com.example.heat.util.enumerian.ComeFrom
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UiUtils.Companion.showToast
import com.example.heat.util.UserIDManager
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class ActiveLevelFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((UserPreferences?) -> ActiveLevelViewModelFactory) by factory()

    private lateinit var viewModel: ActiveLevelViewModel

    private var _binding: FragmentActiveLevelBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveLevelBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { ActiveLevelFragmentArgs.fromBundle(it) }
        val userPref = safeArgs?.userPreferences
        val comeFrom = safeArgs?.comeFrom

        viewModel = ViewModelProvider(
            this,
            viewModelFactoryInstanceFactory(userPref)
        )[ActiveLevelViewModel::class.java]

        var isFromProfile = false

        if (userPref != null) {
            setData(userPref)
        }
        if (comeFrom == ComeFrom.PROFILE) isFromProfile = true

        bindUI(isFromProfile, userPref)
    }

    private fun setData(userPreferences: UserPreferences) {
        binding.apply {
            when (userPreferences.activeLevel) {
                ActiveLevel.SEDENTARY -> toggleButtonGroupActiveLevel.check(R.id.sedentary)
                ActiveLevel.LIGHTLY -> toggleButtonGroupActiveLevel.check(R.id.lightly)
                ActiveLevel.MODERATELY -> toggleButtonGroupActiveLevel.check(R.id.moderately)
                ActiveLevel.VERY -> toggleButtonGroupActiveLevel.check(R.id.very)
                ActiveLevel.EXTRA -> toggleButtonGroupActiveLevel.check(R.id.extra)
            }
        }
    }

    private fun bindUI(isFromProfile: Boolean, userPreference: UserPreferences?) = launch {
        if (userPreference == null) {
            return@launch
        }

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
            binding.toolbarLayout.progressView.progress = 20f
            binding.toolbarLayout.progressView.labelText = "1 out of 6 completed"
        }

        binding.apply {
            if (isFromProfile) {
                toolbarLayout.save.setOnClickListener {
                    val user = saveData(binding, userPreference, it)

                    //send updated user pref to server
                    if(UiUtils.isNetworkConnected(requireActivity()))
                        viewModel.updateUserPreferencesToServer(user)
                }
            } else {
                next.setOnClickListener {
                    saveData(binding, userPreference, it)
                }
                previous.setOnClickListener {
                    saveData(binding, userPreference, it)
                }
            }
            toolbarLayout.backArrow.setOnClickListener {
                saveData(binding, userPreference, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is ActiveLevelViewModel.ActiveLevelTransactionsEvents.NavigateToAbstractGoalScreen -> {
                        Log.e("USER_PREF_PERSONAL_DATA", userPreference.toString())
                        when (isFromProfile) {
                            true -> {
                                requireActivity().onBackPressed()
                            }
                            false -> {
                                val action =
                                    ActiveLevelFragmentDirections.actionActiveLevelFragmentToAbstractGoalFragment(
                                        userPreference,
                                        ComeFrom.SURVEY
                                    )
                                findNavController().navigate(action)
                            }
                        }

                    }
                    is ActiveLevelViewModel.ActiveLevelTransactionsEvents.NavigateBackToPersonalDataScreen ->
                        if (!isFromProfile) {
                            val action =
                                ActiveLevelFragmentDirections.actionActiveLevelFragmentToPersonalDataFragment(
                                    userPreference,
                                    ComeFrom.SURVEY
                                )
                            findNavController().navigate(action)
                        }
                    else -> {
                        UiUtils.showSimpleSnackBar(
                            binding.root,
                            getString(R.string.complete_all_parts)
                        )
                    }
                }
            }
        }.exhaustive
    }

    private fun saveData(
        binding: FragmentActiveLevelBinding,
        userPreference: UserPreferences,
        itemView: View
    ) : UserPreferences {
        binding.apply {

            if (toggleButtonGroupActiveLevel.checkedButtonId != View.NO_ID) {
                when {
                    sedentary.isChecked -> userPreference.activeLevel = ActiveLevel.SEDENTARY
                    lightly.isChecked -> userPreference.activeLevel = ActiveLevel.LIGHTLY
                    moderately.isChecked -> userPreference.activeLevel = ActiveLevel.MODERATELY
                    very.isChecked -> userPreference.activeLevel = ActiveLevel.VERY
                    extra.isChecked -> userPreference.activeLevel = ActiveLevel.EXTRA
                }
                if (itemView == next || itemView == toolbarLayout.save || itemView == toolbarLayout.backArrow)
                    viewModel.onNextClicked(userPreference)
                else if (itemView == previous)
                    viewModel.onPreviousClicked(userPreference)

            } else {
                viewModel.shouldShowFillAllPartSnackBar()
            }
        }
        return userPreference
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}