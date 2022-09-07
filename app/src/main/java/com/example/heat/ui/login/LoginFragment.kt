package com.example.heat.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.LoginRequest
import com.example.heat.data.datamodel.user.RegisterRequest
import com.example.heat.databinding.FragmentHomeBinding
import com.example.heat.databinding.FragmentLoginBinding
import com.example.heat.ui.MainActivity
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.home.HomeViewModel
import com.example.heat.ui.home.HomeViewModelFactory
import com.example.heat.ui.profile.ProfileFragmentDirections
import com.example.heat.ui.setting.disease.DiseaseFragmentDirections
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.UiUtils.Companion.isEditTextEmpty
import com.example.heat.util.UiUtils.Companion.isEmailValid
import com.example.heat.util.UiUtils.Companion.saveUserIDToDataStore
import com.example.heat.util.UiUtils.Companion.saveUserLoginStatusToDataStore
import com.example.heat.util.UiUtils.Companion.showSimpleSnackBar
import com.example.heat.util.UiUtils.Companion.showToast
import com.example.heat.util.UserManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class LoginFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: LoginViewModelFactory by instance()

    private lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    @DelicateCoroutinesApi
    private fun bindUI() = launch {
        binding.apply {
            loading.setAnimation(R.raw.loading);
            doNotHaveAccountBtn.setOnClickListener {
                viewModel.navigateToRegisterClicked()
            }
            loginBtn.setOnClickListener {
                if (validInput()) {
                    if (validEmail()) {
                        loading.visibility = View.VISIBLE

                        sendLoginRequest()

                    } else {
                        viewModel.showInvalidInputToast("Your Email format is not correct")
                    }
                } else {
                    viewModel.showInvalidInputToast("Input all data")
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect { event ->
                when (event) {
                    is LoginViewModel.LoginTransactionEvent.NavigateToRegisterScreen -> {
                        val actionAdd =
                            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                        findNavController().navigate(actionAdd)
                    }
                    is LoginViewModel.LoginTransactionEvent.ShowInvalidInputToast -> {
                        showSimpleSnackBar(binding.root, event.str)
                    }
                    is LoginViewModel.LoginTransactionEvent.NavigateToHomeScreen -> {
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                }
            }

        }
    }

    private fun sendLoginRequest() = launch {
        val user = LoginRequest(
            binding.passwordLoginEt.text.toString(),
            binding.emailLoginEt.text.toString(),
        )
        viewModel.setRegisterUser(user)
        viewModel.loginRequest.await()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it.id) {
                    404 ->{
                        binding.loading.visibility = View.GONE
                        showToast(requireContext(), "Username Not Found!")
                    }
                    406 -> {
                        binding.loading.visibility = View.GONE
                        showToast(requireContext(), "Your Password is not correct!")
                    }
                    408 -> {
                        binding.loading.visibility = View.GONE
                        showToast(requireContext(), "Something went wrong in login. Try again later.")
                    }
                    else -> {
                        viewModel.setUserIDLogin(it.id)
                        binding.loading.visibility = View.GONE
                        saveUserLoginStatusToDataStore(requireContext(), true)
                        saveUserIDToDataStore(requireContext(), it.id)
                        viewModel.navigateToHomeScreen()
                    }
                }

            }
        })


    }


    private fun validInput(): Boolean {
        binding.apply {
            return !isEditTextEmpty(emailLoginEt) && !isEditTextEmpty(passwordLoginEt)
        }
    }

    private fun validEmail(): Boolean {
        binding.apply {
            return /*isEmailValid(emailLoginEt.text.toString())*/ true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
