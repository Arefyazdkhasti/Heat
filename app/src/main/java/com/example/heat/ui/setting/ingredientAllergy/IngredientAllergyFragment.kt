package com.example.heat.ui.setting.ingredientAllergy

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
import com.example.heat.databinding.FragmentIngredientAllergyBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.setting.activeLevel.ActiveLevelFragmentArgs
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UserIDManager
import com.example.heat.util.enumerian.ComeFrom
import com.example.heat.util.enumerian.IngredientAllergy
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class IngredientAllergyFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((UserPreferences?) -> IngredientAllergyViewModelFactory) by factory()

    private lateinit var viewModel: IngredientAllergyViewModel

    private var _binding: FragmentIngredientAllergyBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientAllergyBinding.inflate(inflater, container, false)
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
        )[IngredientAllergyViewModel::class.java]

        var isFromProfile = false

        if (userPref != null) {
            setData(userPref)
        }
        if (comeFrom == ComeFrom.PROFILE) isFromProfile = true

        bindUI(isFromProfile, userPref)
    }

    private fun setData(userPreferences: UserPreferences) {
        binding.apply {
            val ingredients = userPreferences.ingredientsAllergy
            if (ingredients.contains(IngredientAllergy.GLUTEN.toString())) allergyChipGroup.check(R.id.chip_gluten)
            if (ingredients.contains(IngredientAllergy.PEANUTS.toString())) allergyChipGroup.check(R.id.chip_peanut)
            if (ingredients.contains(IngredientAllergy.EGG.toString())) allergyChipGroup.check(R.id.chip_egg)
            if (ingredients.contains(IngredientAllergy.FISH.toString())) allergyChipGroup.check(R.id.chip_fish)
            if (ingredients.contains(IngredientAllergy.SHELLFISH.toString())) allergyChipGroup.check(R.id.chip_shellfish)
            if (ingredients.contains(IngredientAllergy.DIARY.toString())) allergyChipGroup.check(R.id.chip_diary)
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
            binding.toolbarLayout.progressView.progress = 80f
            binding.toolbarLayout.progressView.labelText = "4 out of 6 completed"
        }

        binding.apply {
            if (isFromProfile) {
                toolbarLayout.save.setOnClickListener {
                    val user = saveData(binding, userPreference, it)

                    //send updated user pref to server
                    //TODO update ingredient allergy to server
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
                    is IngredientAllergyViewModel.IngredientAllergyTransactionsEvents.NavigateToDiseaseScreen -> {
                        Log.e("USER_PREF_PERSONAL_DATA", userPreference.toString())
                        when (isFromProfile) {
                            true -> {
                                requireActivity().onBackPressed()
                            }
                            false -> {
                                val action =
                                    IngredientAllergyFragmentDirections.actionIngredientAllergyFragmentToDiseaseFragment(
                                        userPreference,
                                        ComeFrom.SURVEY
                                    )
                                findNavController().navigate(action)
                            }
                        }

                    }
                    is IngredientAllergyViewModel.IngredientAllergyTransactionsEvents.NavigateBackToDietTypeScreen ->
                        if (!isFromProfile) {
                            val action =
                                IngredientAllergyFragmentDirections.actionIngredientAllergyFragmentToDietTypeFragment(
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
        binding: FragmentIngredientAllergyBinding,
        userPreference: UserPreferences,
        itemView: View
    ): UserPreferences {
        binding.apply {

            userPreference.id = UiUtils.getUserIDFromDataStore(
                requireContext(),
                viewLifecycleOwner
            )
            val selectedItems = arrayListOf<IngredientAllergy>()
            userPreference.ingredientsAllergy.clear()

            if (chipGluten.isChecked) selectedItems.add(IngredientAllergy.GLUTEN)
            if (chipPeanut.isChecked) selectedItems.add(IngredientAllergy.PEANUTS)
            if (chipEgg.isChecked) selectedItems.add(IngredientAllergy.EGG)
            if (chipFish.isChecked) selectedItems.add(IngredientAllergy.FISH)
            if (chipShellfish.isChecked) selectedItems.add(IngredientAllergy.SHELLFISH)
            if (chipDiary.isChecked) selectedItems.add(IngredientAllergy.DIARY)

            for (item in selectedItems){
                if(!userPreference.ingredientsAllergy.contains(item.toString()))
                    userPreference.ingredientsAllergy.add(item.toString())
            }
            if (itemView == next || itemView == toolbarLayout.save || itemView == toolbarLayout.backArrow)
                viewModel.onNextClicked(userPreference)
            else if (itemView == previous)
                viewModel.onPreviousClicked(userPreference)

        }
        return userPreference
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}