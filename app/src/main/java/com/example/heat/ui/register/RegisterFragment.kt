package com.example.heat.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.databinding.FragmentLoginBinding
import com.example.heat.databinding.FragmentRegisterBinding
import com.example.heat.ui.MainActivity
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.login.LoginFragmentDirections
import com.example.heat.ui.login.LoginViewModel
import com.example.heat.ui.login.LoginViewModelFactory
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.isEditTextEmpty
import com.example.heat.util.UiUtils.Companion.isEmailValid
import com.example.heat.util.UiUtils.Companion.saveUserLoginStatusToDataStore
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
                        //TODO send server request
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            loading.visibility = View.GONE
                            viewModel.navigateToSurveyScreen()
                        }, 5000)
                        saveUserLoginStatusToDataStore(requireContext(), true)

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

    private fun validInput(): Boolean {
        binding.apply {
            return !isEditTextEmpty(emailRegisterEt) && !isEditTextEmpty(passwordRegisterEt) && !isEditTextEmpty(usernameRegisterEt)
        }
    }

    private fun validEmail(): Boolean {
        binding.apply {
            return isEmailValid(emailRegisterEt.text.toString())
        }
    }
}