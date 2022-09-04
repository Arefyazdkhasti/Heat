package com.example.heat.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.databinding.FragmentProfileBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.util.UiUtils.Companion.saveUserLoginStatusToDataStore
import com.example.heat.util.enumerian.*
import com.example.heat.util.exhaustive
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Exception
import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.os.Build


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

    @DelicateCoroutinesApi
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
            UserDietType.ANY_THING,
            arrayListOf(),
            arrayListOf()
        )
        //load User
        viewModel.getUserPreference.await().observe(viewLifecycleOwner, Observer { userPref ->
            if (userPref == null) return@Observer

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
            usernameLayout.setOnClickListener {
                viewModel.personalDataClicked()
            }
            generalSettingLayout.setOnClickListener {
                viewModel.settingClicked()
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
            logoutLayout.setOnClickListener {
                showConfirmLogoutDialog()
            }
            neededAmountsOfNutritionLayout.setOnClickListener {
                viewModel.dailyNutritionClicked()
            }
            likedFoodsLayout.setOnClickListener {
                viewModel.likedFoodsClicked()
            }
            contactUsLayout.setOnClickListener{
                viewModel.contactUsClicked()
            }
            upgradeToPremiumLayout.setOnClickListener {
                viewModel.upgradeToPremiumClicked()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.profileEvent.collect { event ->
                when (event) {
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToSettingScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToPersonalDataScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment(
                                userPreference,
                                ComeFrom.PROFILE
                            )
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToActiveLevelScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToActiveLevelFragment(
                                userPreference,
                                ComeFrom.PROFILE
                            )
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToDietTypeToScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToDietTypeFragment(
                                userPreference,
                                ComeFrom.PROFILE
                            )
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToAbstractGoalScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToAbstractGoalFragment(
                                userPreference,
                                ComeFrom.PROFILE
                            )
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToIngredientAllergyScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToIngredientAllergyFragment(
                                userPreference,
                                ComeFrom.PROFILE
                            )
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToDiseaseScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToDiseaseFragment(
                                userPreference,
                                ComeFrom.PROFILE
                            )
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToLoginScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToDailyNutritionScreen ->{
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToDailyNutritionFragment(userPreference,ComeFrom.PROFILE)
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToLikedFoodsScreen -> {
                        val actionAdd =
                            ProfileFragmentDirections.actionProfileFragmentToLikedFoodsFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToContactUsScreen -> showContactUsDialog()
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToUpgradeToPremiumScreen -> showUpgradeToPremiumDialog()
                }
            }
        }.exhaustive
    }

    @DelicateCoroutinesApi
    private fun showConfirmLogoutDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialog.apply {
            setTitle("Logout")
            setIcon(R.drawable.ic_logout)
            setMessage("Do you really want to logout of your account? This action will delete all your local data.")
            setPositiveButton(
                "Yes"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                context.cacheDir.deleteRecursively()
                clearAppData()
                saveUserLoginStatusToDataStore(requireContext(),false)
                viewModel.logoutClicked()
            }
            setNeutralButton(
                "No"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            show()
        }
    }
    private fun clearAppData() = launch {
        viewModel.deleteDataInRoom.await()
    }

    private fun showContactUsDialog(){
        val builder = MaterialAlertDialogBuilder(requireContext(),R.style.AlertDialogTheme).create()
        val view = layoutInflater.inflate(R.layout.dialog_contact_us,null)
        builder.setView(view)
        builder.show()
    }

    private fun showUpgradeToPremiumDialog(){
        val builder = MaterialAlertDialogBuilder(requireContext(),R.style.AlertDialogTheme).create()
        val view = layoutInflater.inflate(R.layout.dialog_upgrade_to_premium,null)
        builder.setView(view)
        builder.show()
    }
}