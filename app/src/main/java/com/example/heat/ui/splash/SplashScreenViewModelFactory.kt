package com.example.heat.ui.splash

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.ui.login.LoginViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashScreenViewModel() as T
    }
}