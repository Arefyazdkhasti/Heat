package com.example.heat.ui.setting.abstractGoal

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
import com.example.heat.databinding.FragmentAbstractGoalBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.setting.activeLevel.ActiveLevelFragmentArgs
import com.example.heat.util.UiUtils
import com.example.heat.util.enumerian.AbstractGoal
import com.example.heat.util.enumerian.ComeFrom
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class AbstractGoalFragment  : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((UserPreferences?) -> AbstractGoalViewModelFactory) by factory()

    private lateinit var viewModel: AbstractGoalViewModel

    private var _binding: FragmentAbstractGoalBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbstractGoalBinding.inflate(inflater, container, false)
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
        )[AbstractGoalViewModel::class.java]

        var isFromProfile = false

        if (userPref != null) {
            setData(userPref)
        }
        if(comeFrom == ComeFrom.PROFILE) isFromProfile = true

        bindUI(isFromProfile , userPref)
    }

    private fun setData(userPreferences: UserPreferences) {
        binding.apply {
            when (userPreferences.abstractGoal) {
                AbstractGoal.LOSE -> toggleButtonGroupAbstractGoal.check(R.id.lose)
                AbstractGoal.MAINTAIN -> toggleButtonGroupAbstractGoal.check(R.id.maintain)
                AbstractGoal.GAIN -> toggleButtonGroupAbstractGoal.check(R.id.gain)
            }
        }
    }

    private fun bindUI(isFromProfile: Boolean, userPreference: UserPreferences?) = launch {
        if(userPreference == null){
            UiUtils.showToast(requireContext(), "UserPref is null")
            return@launch
        }

        if (isFromProfile) {
            binding.navigationLayout.visibility = View.GONE
            binding.save.visibility = View.VISIBLE
        } else {
            binding.navigationLayout.visibility = View.VISIBLE
            binding.save.visibility = View.GONE
            binding.backArrow.visibility = View.GONE
        }

        binding.apply {
            if (isFromProfile) {
                save.setOnClickListener {
                    saveData(binding, userPreference, it)
                }
            } else {
                next.setOnClickListener {
                    saveData(binding, userPreference , it)
                }
                previous.setOnClickListener {
                    saveData(binding, userPreference, it)
                }
            }
            backArrow.setOnClickListener {
                saveData(binding, userPreference, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is AbstractGoalViewModel.AbstractGoalTransactionsEvents.NavigateToDietTypeScreen -> {
                        Log.e("USER_PREF_PERSONAL_DATA", userPreference.toString())
                        when (isFromProfile) {
                            true -> {
                                requireActivity().onBackPressed()
                            }
                            false -> {
                                val action =
                                    AbstractGoalFragmentDirections.actionAbstractGoalFragmentToDietTypeFragment(
                                        userPreference,
                                        ComeFrom.SURVEY
                                    )
                                findNavController().navigate(action)
                            }
                        }

                    }
                    is AbstractGoalViewModel.AbstractGoalTransactionsEvents.NavigateBackToActiveLevelScreen ->
                        if (!isFromProfile) {
                            val action =
                                AbstractGoalFragmentDirections.actionAbstractGoalFragmentToActiveLevelFragment(
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

    private fun saveData(binding: FragmentAbstractGoalBinding, userPreference: UserPreferences, itemView: View) {
        binding.apply {

            if (toggleButtonGroupAbstractGoal.checkedButtonId != View.NO_ID) {
                when {
                    lose.isChecked -> userPreference.abstractGoal = AbstractGoal.LOSE
                    maintain.isChecked -> userPreference.abstractGoal = AbstractGoal.MAINTAIN
                    gain.isChecked -> userPreference.abstractGoal = AbstractGoal.GAIN
                }
                if(itemView == next || itemView == save || itemView == backArrow)
                    viewModel.onNextClicked(userPreference)
                else if(itemView == previous)
                    viewModel.onPreviousClicked(userPreference)

            } else {
                viewModel.shouldShowFillAllPartSnackBar()
            }
        }
    }
}