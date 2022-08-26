package com.example.heat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.data.repository.RecipesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel(){

    private val loginTransactionEvent = Channel<LoginTransactionEvent>()
    val loginEvent = loginTransactionEvent.receiveAsFlow()

    fun navigateToRegisterClicked() = viewModelScope.launch {
        loginTransactionEvent.send(LoginTransactionEvent.NavigateToRegisterScreen)
    }
    fun showInvalidInputToast(str:String) = viewModelScope.launch {
        loginTransactionEvent.send(LoginTransactionEvent.ShowInvalidInputToast(str))
    }
    fun navigateToHomeScreen() = viewModelScope.launch {
        loginTransactionEvent.send(LoginTransactionEvent.NavigateToHomeScreen)
    }

    sealed class LoginTransactionEvent {
        object NavigateToRegisterScreen : LoginTransactionEvent()
        data class ShowInvalidInputToast(val str:String): LoginTransactionEvent()
        object NavigateToHomeScreen: LoginTransactionEvent()
    }
}