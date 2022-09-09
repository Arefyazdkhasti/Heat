package com.example.heat.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.databinding.FragmentSplashScreenBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.util.UiUtils.Companion.dataStore
import com.example.heat.util.manager.UserManager
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: SplashScreenViewModelFactory by instance()

    private lateinit var viewModel: SplashScreenViewModel

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashScreenViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI() = launch {
        val handler = Handler()
        handler.postDelayed(Runnable {
            checkForNextDestination()
        }, 5000)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.splashEvent.collect { event ->
                when (event) {
                    is SplashScreenViewModel.SplashTransactionEvent.NavigateToHomeScreen -> {
                        val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                    is SplashScreenViewModel.SplashTransactionEvent.NavigateToLoginScreen -> {
                        val actionAdd =
                            SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                        findNavController().navigate(actionAdd)
                    }
                }
            }
        }
    }

    private fun checkForNextDestination() {
        val dataStore = context?.dataStore
        if (dataStore != null) {
            val userManager = UserManager(dataStore)
            userManager.userIsLoggedInFlow.asLiveData().observe(viewLifecycleOwner, {
                if (it != null) {
                    if (it)
                        viewModel.navigateToHome()
                    else
                        viewModel.navigateToLogin()
                }else
                    viewModel.navigateToLogin()
            })
        }else
            viewModel.navigateToLogin()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}