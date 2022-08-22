package com.example.heat.ui.setting.dietType

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
import com.example.heat.databinding.FragmentDietTypeBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.setting.activeLevel.ActiveLevelFragmentArgs
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.getColor
import com.example.heat.util.enumerian.ComeFrom
import com.example.heat.util.enumerian.DietType
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
                DietType.ANY_THING -> anythingCardView.setBackgroundColor(selectColor)
                DietType.Vegetarian -> vegetarianCardView.setBackgroundColor(selectColor)
                DietType.Vegan -> veganCardView.setBackgroundColor(selectColor)
                DietType.Muslim -> muslimCardView.setBackgroundColor(selectColor)
                DietType.Kosher -> kosherCardView.setBackgroundColor(selectColor)
            }
        }
    }

    private fun bindUI(isFromProfile: Boolean, userPreference: UserPreferences?) = launch {


        if (userPreference == null) {
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

            val selectColor = getColor(requireContext(), R.color.gray)
            val disselectColor = getColor(requireContext(), R.color.white)
            var selectedDietType = DietType.ANY_THING

            //TODO move to a separate function
            anythingCardView.setOnClickListener {
                selectedDietType = DietType.ANY_THING
                anythingCardView.setBackgroundColor(selectColor)
                veganCardView.setBackgroundColor(disselectColor)
                vegetarianCardView.setBackgroundColor(disselectColor)
                muslimCardView.setBackgroundColor(disselectColor)
                kosherCardView.setBackgroundColor(disselectColor)
            }
            vegetarianCardView.setOnClickListener {
                selectedDietType = DietType.Vegetarian
                anythingCardView.setBackgroundColor(disselectColor)
                veganCardView.setBackgroundColor(disselectColor)
                vegetarianCardView.setBackgroundColor(selectColor)
                muslimCardView.setBackgroundColor(disselectColor)
                kosherCardView.setBackgroundColor(disselectColor)
            }
            veganCardView.setOnClickListener {
                selectedDietType = DietType.Vegan
                anythingCardView.setBackgroundColor(disselectColor)
                veganCardView.setBackgroundColor(selectColor)
                vegetarianCardView.setBackgroundColor(disselectColor)
                muslimCardView.setBackgroundColor(disselectColor)
                kosherCardView.setBackgroundColor(disselectColor)
            }
            muslimCardView.setOnClickListener {
                selectedDietType = DietType.Muslim
                anythingCardView.setBackgroundColor(disselectColor)
                veganCardView.setBackgroundColor(disselectColor)
                vegetarianCardView.setBackgroundColor(disselectColor)
                muslimCardView.setBackgroundColor(selectColor)
                kosherCardView.setBackgroundColor(disselectColor)
            }
            kosherCardView.setOnClickListener {
                selectedDietType = DietType.Kosher
                anythingCardView.setBackgroundColor(disselectColor)
                veganCardView.setBackgroundColor(disselectColor)
                vegetarianCardView.setBackgroundColor(disselectColor)
                muslimCardView.setBackgroundColor(disselectColor)
                kosherCardView.setBackgroundColor(selectColor)
            }

            if (isFromProfile) {
                save.setOnClickListener {
                    saveData(binding, userPreference, it, selectedDietType)
                }
            } else {
                next.setOnClickListener {
                    saveData(binding, userPreference, it, selectedDietType)
                }
                previous.setOnClickListener {
                    saveData(binding, userPreference, it, selectedDietType)
                }
            }
            backArrow.setOnClickListener {
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

    /*private fun selectDietType() : DietType{

            return selectedDietType
        }
    }*/

    private fun saveData(
        binding: FragmentDietTypeBinding,
        userPreference: UserPreferences,
        itemView: View,
        dietType: DietType
    ) {
        binding.apply {

            if (dietType != null) {
                userPreference.dietType = dietType
                if (itemView == next || itemView == save || itemView == backArrow)
                    viewModel.onNextClicked(userPreference)
                else if (itemView == previous)
                    viewModel.onPreviousClicked(userPreference)

            } else {
                viewModel.shouldShowFillAllPartSnackBar()
            }
        }
    }
}