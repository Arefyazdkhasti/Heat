package com.example.heat.ui.setting.dietType

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
import com.example.heat.databinding.FragmentDietTypeBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.setting.activeLevel.ActiveLevelFragmentArgs
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UiUtils.Companion.getColor
import com.example.heat.util.UserIDManager
import com.example.heat.util.enumerian.ComeFrom
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.UserDietType
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class DietTypeFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((UserPreferences?) -> DietTypeViewModelFactory) by factory()

    private lateinit var viewModel: DietTypeViewModel

    private var _binding: FragmentDietTypeBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietTypeBinding.inflate(inflater, container, false)
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
        )[DietTypeViewModel::class.java]

        var isFromProfile = false

        if (userPref != null) {
            setData(userPref)
        }
        if (comeFrom == ComeFrom.PROFILE) isFromProfile = true

        bindUI(isFromProfile, userPref)
    }

    private fun setData(userPreferences: UserPreferences) {
        val selectColor = getColor(requireContext(), R.color.gray)

        binding.apply {
            when (userPreferences.dietType) {
                UserDietType.ANY_THING -> anythingCardView.setBackgroundColor(selectColor)
                UserDietType.VEGETARIAN -> vegetarianCardView.setBackgroundColor(selectColor)
                UserDietType.VEGAN -> veganCardView.setBackgroundColor(selectColor)
                UserDietType.HALAL -> muslimCardView.setBackgroundColor(selectColor)
                UserDietType.KOSHER -> kosherCardView.setBackgroundColor(selectColor)
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
            binding.toolbarLayout.progressView.progress = 60f
            binding.toolbarLayout.progressView.labelText = "3 out of 6 completed"
        }

        binding.apply {

            val selectColor = getColor(requireContext(), R.color.gray)
            val disSelectColor = getColor(requireContext(), R.color.pure_white)

            val selectedDietType = selectionDecision(selectColor, disSelectColor)

            if (isFromProfile) {
                toolbarLayout.save.setOnClickListener {
                    val user = saveData(binding, userPreference, it, selectedDietType)

                    //send updated user pref to server
                    if (UiUtils.isNetworkConnected(requireActivity()))
                        viewModel.updateUserPreferencesToServer(user)
                }
            } else {
                next.setOnClickListener {
                    saveData(binding, userPreference, it, selectedDietType)
                }
                previous.setOnClickListener {
                    saveData(binding, userPreference, it, selectedDietType)
                }
            }
            toolbarLayout.backArrow.setOnClickListener {
                saveData(binding, userPreference, it, selectedDietType)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is DietTypeViewModel.DietTypeTransactionsEvents.NavigateToIngredientAllergyScreen -> {
                        Log.e("USER_PREF_PERSONAL_DATA", userPreference.toString())
                        when (isFromProfile) {
                            true -> {
                                requireActivity().onBackPressed()
                            }
                            false -> {
                                val action =
                                    DietTypeFragmentDirections.actionDietTypeFragmentToIngredientAllergyFragment(
                                        userPreference,
                                        ComeFrom.SURVEY
                                    )
                                findNavController().navigate(action)
                            }
                        }

                    }
                    is DietTypeViewModel.DietTypeTransactionsEvents.NavigateBackToAbstractGoalScreen ->
                        if (!isFromProfile) {
                            val action =
                                DietTypeFragmentDirections.actionDietTypeFragmentToAbstractGoalFragment(
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

    private fun selectionDecision(selectColor: Int, disSelectColor: Int): UserDietType {
        var selectedDietType = UserDietType.ANY_THING
        binding.apply {
            anythingCardView.setOnClickListener {
                selectedDietType = UserDietType.ANY_THING
                anythingCardView.setBackgroundColor(selectColor)
                veganCardView.setBackgroundColor(disSelectColor)
                vegetarianCardView.setBackgroundColor(disSelectColor)
                muslimCardView.setBackgroundColor(disSelectColor)
                kosherCardView.setBackgroundColor(disSelectColor)
            }
            vegetarianCardView.setOnClickListener {
                selectedDietType = UserDietType.VEGETARIAN
                anythingCardView.setBackgroundColor(disSelectColor)
                veganCardView.setBackgroundColor(disSelectColor)
                vegetarianCardView.setBackgroundColor(selectColor)
                muslimCardView.setBackgroundColor(disSelectColor)
                kosherCardView.setBackgroundColor(disSelectColor)
            }
            veganCardView.setOnClickListener {
                selectedDietType = UserDietType.VEGAN
                anythingCardView.setBackgroundColor(disSelectColor)
                veganCardView.setBackgroundColor(selectColor)
                vegetarianCardView.setBackgroundColor(disSelectColor)
                muslimCardView.setBackgroundColor(disSelectColor)
                kosherCardView.setBackgroundColor(disSelectColor)
            }
            muslimCardView.setOnClickListener {
                selectedDietType = UserDietType.HALAL
                anythingCardView.setBackgroundColor(disSelectColor)
                veganCardView.setBackgroundColor(disSelectColor)
                vegetarianCardView.setBackgroundColor(disSelectColor)
                muslimCardView.setBackgroundColor(selectColor)
                kosherCardView.setBackgroundColor(disSelectColor)
            }
            kosherCardView.setOnClickListener {
                selectedDietType = UserDietType.KOSHER
                anythingCardView.setBackgroundColor(disSelectColor)
                veganCardView.setBackgroundColor(disSelectColor)
                vegetarianCardView.setBackgroundColor(disSelectColor)
                muslimCardView.setBackgroundColor(disSelectColor)
                kosherCardView.setBackgroundColor(selectColor)
            }
        }
        return selectedDietType
    }

    private fun saveData(
        binding: FragmentDietTypeBinding,
        userPreference: UserPreferences,
        itemView: View,
        dietType: UserDietType
    ): UserPreferences {
        binding.apply {

            if (dietType != null) {
                userPreference.dietType = dietType
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

