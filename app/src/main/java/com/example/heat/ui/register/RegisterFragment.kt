package com.example.heat.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.RegisterRequest
import com.example.heat.databinding.FragmentRegisterBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.isEditTextEmpty
import com.example.heat.util.UiUtils.Companion.isEmailValid
import com.example.heat.util.UiUtils.Companion.saveUserIDToDataStore
import com.example.heat.util.UiUtils.Companion.saveUserLoginStatusToDataStore
import com.example.heat.util.UiUtils.Companion.showToast
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class RegisterFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var viewModel: RegisterViewModel

    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        binding.apply {
            loading.setAnimation(R.raw.loading);
            alreadyHaveAccountBtn.setOnClickListener {
                viewModel.navigateToRegisterClicked()
            }

            registerBtn.setOnClickListener {
                if (validInput()) {
                    if (validEmail()) {
                        loading.visibility = View.VISIBLE

                        sendRegisterRequest()

                    } else {
                        viewModel.showInvalidInputToast("Your Email format is not correct")
                    }
                } else {
                    viewModel.showInvalidInputToast("Input all data")
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.registerEvent.collect { event ->
                when (event) {
                    is RegisterViewModel.RegisterTransactionEvent.NavigateToLoginScreen -> {
                        val actionAdd =
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is RegisterViewModel.RegisterTransactionEvent.ShowInvalidInputToast -> {
                        UiUtils.showSimpleSnackBar(binding.root, event.str)
                    }
                    is RegisterViewModel.RegisterTransactionEvent.NavigateToSurveyScreen -> {
                        val actionAdd =
                            RegisterFragmentDirections.actionRegisterFragmentToSurveyFragment()
                        findNavController().navigate(actionAdd)
                    }
                }
            }

        }
    }

    private fun sendRegisterRequest() = launch {
        val user = RegisterRequest(
            binding.emailRegisterEt.text.toString(),
            binding.passwordRegisterEt.text.toString(),
            binding.usernameRegisterEt.text.toString(),
        )
        viewModel.setRegisterUser(user)
        //TODO send server request
        viewModel.registerRequest.await()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.loading.visibility = View.GONE
                when (it.id){
                    409 -> {
                        showToast(requireContext(), "Username already taken. Choose a new one.")
                    }
                    408 -> {
                        showToast(requireContext(), "Something went wrong in Register. Try again later.")
                    }
                    else ->{
                        viewModel.navigateToSurveyScreen()
                        saveUserLoginStatusToDataStore(requireContext(), true)
                        saveUserIDToDataStore(requireContext(),it.id)
                    }
                }

            }
        })
    }

    private fun validInput(): Boolean {
        binding.apply {
            return !isEditTextEmpty(emailRegisterEt) && !isEditTextEmpty(passwordRegisterEt) && !isEditTextEmpty(
                usernameRegisterEt
            )
        }
    }

    private fun validEmail(): Boolean {
        binding.apply {
            return isEmailValid(emailRegisterEt.text.toString())
        }
    }
}