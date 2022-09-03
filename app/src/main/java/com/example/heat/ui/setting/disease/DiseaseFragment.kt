package com.example.heat.ui.setting.disease

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.databinding.FragmentDiseaseBinding
import com.example.heat.ui.MainActivity
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.setting.activeLevel.ActiveLevelFragmentArgs
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UiUtils.Companion.showToast
import com.example.heat.util.UserIDManager
import com.example.heat.util.enumerian.ComeFrom
import com.example.heat.util.enumerian.Disease
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory


class DiseaseFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((UserPreferences?) -> DiseaseViewModelFactory) by factory()

    private lateinit var viewModel: DiseaseViewModel

    private var _binding: FragmentDiseaseBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiseaseBinding.inflate(inflater, container, false)
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
        )[DiseaseViewModel::class.java]

        var isFromProfile = false

        if (userPref != null) {
            setData(userPref)
        }
        if (comeFrom == ComeFrom.PROFILE) isFromProfile = true

        bindUI(isFromProfile, userPref)
    }

    private fun setData(userPreferences: UserPreferences) {
        binding.apply {
            val diseases = userPreferences.disease
            if (diseases.contains(Disease.Diabetes.toString())) diseaseChipGroup.check(R.id.chip_diabetes)
            if (diseases.contains(Disease.Osteoporosis.toString())) diseaseChipGroup.check(R.id.chip_osteoporosis)
            if (diseases.contains(Disease.HeartDisease.toString())) diseaseChipGroup.check(R.id.chip_heart_disease)
            if (diseases.contains(Disease.HighBloodPressure.toString())) diseaseChipGroup.check(R.id.chip_high_blood_pressure)
            if (diseases.contains(Disease.KidneyDisease.toString())) diseaseChipGroup.check(R.id.chip_kidney_disease)
            if (diseases.contains(Disease.Anemia.toString())) diseaseChipGroup.check(R.id.chip_anemia)
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
            binding.toolbarLayout.progressView.progress = 100f
            binding.toolbarLayout.progressView.labelText = "5 out of 6 completed"
        }

        binding.apply {
            if (isFromProfile) {
                toolbarLayout.save.setOnClickListener {
                    val user = saveData(binding, userPreference, it)

                    //send updated user pref to server
                    //TODO update disease to server
                    //if(UiUtils.isNetworkConnected(requireActivity()))
                        //viewModel.updateUserPreferencesToServer(user)
                }
            } else {
                finish.setOnClickListener {
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
                    is DiseaseViewModel.DiseaseTransactionsEvents.NavigateToHomeScreen -> {
                        Log.e("USER_PREF_PERSONAL_DATA", userPreference.toString())
                        when (isFromProfile) {
                            true -> {
                                requireActivity().onBackPressed()
                            }
                            false -> {
                                val action =
                                    DiseaseFragmentDirections.actionDiseaseFragmentToHomeFragment()
                                findNavController().navigate(action)
                            }
                        }

                    }
                    is DiseaseViewModel.DiseaseTransactionsEvents.NavigateBackToIngredientAllergyScreen ->
                        if (!isFromProfile) {
                            val action =
                                DiseaseFragmentDirections.actionDiseaseFragmentToIngredientAllergyFragment(
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
        binding: FragmentDiseaseBinding,
        userPreference: UserPreferences,
        itemView: View
    ): UserPreferences {
        launch {
            userPreference.id = getUserIDFromDataStore()
            binding.apply {
                val selectedItems = arrayListOf<Disease>()
                userPreference.disease.clear()

                if (chipDiabetes.isChecked) selectedItems.add(Disease.Diabetes)
                if (chipOsteoporosis.isChecked) selectedItems.add(Disease.Osteoporosis)
                if (chipHeartDisease.isChecked) selectedItems.add(Disease.HeartDisease)
                if (chipHighBloodPressure.isChecked) selectedItems.add(Disease.HighBloodPressure)
                if (chipKidneyDisease.isChecked) selectedItems.add(Disease.KidneyDisease)
                if (chipAnemia.isChecked) selectedItems.add(Disease.Anemia)

                for (item in selectedItems) {
                    if (!userPreference.disease.contains(item.toString()))
                        userPreference.disease.add(item.toString())
                }
                if (itemView == toolbarLayout.save || itemView == toolbarLayout.backArrow)
                    viewModel.onNextClicked(userPreference)
                else if (itemView == previous)
                    viewModel.onPreviousClicked(userPreference)
                else if (itemView == finish) {
                    //save userPreference to Room database
                    viewModel.saveUserPreferences(userPreference)
                    viewModel.setCurrentUserProf(userPreference)
                    viewModel.sendUserPreferenceRequest.await()
                        ?.observe(viewLifecycleOwner, Observer {
                            if (it != null)
                                viewModel.onNextClicked(userPreference)

                        })
                }
            }
        }
        return userPreference
    }

    private fun getUserIDFromDataStore(): Int {
        val dataStore = context?.dataStore
        var id = 0
        if (dataStore != null) {
            val userManager = UserIDManager(dataStore)
            userManager.userIDFlow.asLiveData().observe(viewLifecycleOwner, {
                if (it != null) {
                    id = it
                }
            })
        }
        return id
    }
}