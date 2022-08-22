package com.example.heat.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.databinding.FragmentProfileBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.util.enumerian.*
import com.example.heat.util.exhaustive
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ProfileFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: ProfileViewModelFactory by instance()

    private lateinit var viewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {

        var userPreference = UserPreferences(
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
        //load User
        viewModel.getUserPreference.await().observe(viewLifecycleOwner, Observer { userPref ->
            if(userPref == null) return@Observer

            userPreference = userPref
            binding.apply {
                username.text = userPref.name
                activeLevel.text = userPref.activeLevel.toString()
                dietType.text = userPref.dietType.toString()
                abstractGoal.text = userPref.abstractGoal.toString()
                ingredientAllergy.text = userPref.ingredientsAllergy.size.toString()
                disease.text = userPref.disease.size.toString()
            }

        })

        binding.apply {
            rightArrowIcon.setOnClickListener {
                viewModel.personalDataClicked()
            }
            activeLevelLayout.setOnClickListener {
                viewModel.activeLevelClicked()
            }
            dietTypeLayout.setOnClickListener {
                viewModel.dietTypeClicked()
            }
            abstractGoalLayout.setOnClickListener {
                viewModel.abstractGoalClicked()
            }
            ingredientAllergyLayout.setOnClickListener {
                viewModel.ingredientAllergyClicked()
            }
            diseaseLayout.setOnClickListener {
                viewModel.diseaseClicked()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.profileEvent.collect { event ->
                when (event) {
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToPersonalDataScreen -> {
                        val actionAdd = ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment(userPreference, ComeFrom.PROFILE)
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToActiveLevelScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToActiveLevelFragment(userPreference, ComeFrom.PROFILE)
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToDietTypeToScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToDietTypeFragment(userPreference, ComeFrom.PROFILE)
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToAbstractGoalScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToAbstractGoalFragment(userPreference, ComeFrom.PROFILE)
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToIngredientAllergyScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToIngredientAllergyFragment(userPreference, ComeFrom.PROFILE)
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToDiseaseScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToDiseaseFragment(userPreference, ComeFrom.PROFILE)
                        findNavController().navigate(actionAdd)
                    }
                }
            }
        }.exhaustive
    }
}