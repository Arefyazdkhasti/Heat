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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
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
import com.example.heat.util.UiUtils.Companion.saveUserLoginStatusToDataStore
import com.example.heat.util.UiUtils.Companion.showSimpleSnackBar
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
                        //TODO send server request
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            // Actions to do after 10 seconds
                            loading.visibility = View.GONE
                            //TODO get user id from server then get userPreferences from server and save it in roomDB
                            viewModel.navigateToHomeScreen()

                            saveUserLoginStatusToDataStore(requireContext(), true)

                        }, 5000)

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

    private fun validInput(): Boolean {
        binding.apply {
            return !isEditTextEmpty(emailLoginEt) && !isEditTextEmpty(passwordLoginEt)
        }
    }

    private fun validEmail(): Boolean {
        binding.apply {
            return isEmailValid(emailLoginEt.text.toString())
        }
    }

}
