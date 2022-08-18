package com.example.heat.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.databinding.FragmentProfileBinding
import com.example.heat.databinding.FragmentSearchBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.search.SearchViewModel
import com.example.heat.ui.search.SearchViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
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

    @DelicateCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {

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
                viewModel.abstarctGoalClicked()
            }
            ingredientAllergyLayout.setOnClickListener {
                viewModel.ingredientAllergyClicked()
            }
            diseaseLayout.setOnClickListener {
                viewModel.diseaeClicked()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.profileEvent.collect{event ->
                when (event){
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToPersonalDataScreen ->{
                        val actionAdd = ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToActiveLevelScreen ->{
                        val actionAdd = ProfileFragmentDirections.actionProfileFragmentToActivrlevelFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToDietTypeToScreen ->{
                        val actionAdd = ProfileFragmentDirections.actionProfileFragmentToDietTypeFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToAbstractGoalScreen ->{
                        val actionAdd = ProfileFragmentDirections.actionProfileFragmentToAbstractGoalFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToIngredientAllergyScreen ->{
                        val actionAdd = ProfileFragmentDirections.actionProfileFragmentToIngredientAllergyFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is ProfileViewModel.ProfileTransactionEvent.NavigateToDiseaseScreen ->{
                        val actionAdd = ProfileFragmentDirections.actionProfileFragmentToDiseaseFragment()
                        findNavController().navigate(actionAdd)
                    }
                }
            }
        }
    }
}