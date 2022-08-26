package com.example.heat.ui.splash

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModel(): ViewModel() {

    private val splashTransactionEvent = Channel<SplashTransactionEvent>()
    val splashEvent = splashTransactionEvent.receiveAsFlow()

    fun navigateToHome() = viewModelScope.launch {
        splashTransactionEvent.send(SplashTransactionEvent.NavigateToHomeScreen)
    }

    fun navigateToLogin() = viewModelScope.launch {
        splashTransactionEvent.send(SplashTransactionEvent.NavigateToLoginScreen)
    }

    sealed class SplashTransactionEvent {
        object NavigateToHomeScreen : SplashTransactionEvent()
        object NavigateToLoginScreen : SplashTransactionEvent()
    }
}