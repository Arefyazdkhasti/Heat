package com.example.heat.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.data.repository.RecipesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private val registerTransactionEvent = Channel<RegisterTransactionEvent>()
    val registerEvent = registerTransactionEvent.receiveAsFlow()

    fun navigateToRegisterClicked() = viewModelScope.launch {
        registerTransactionEvent.send(RegisterTransactionEvent.NavigateToLoginScreen)
    }
    fun showInvalidInputToast(str:String) = viewModelScope.launch {
        registerTransactionEvent.send(RegisterTransactionEvent.ShowInvalidInputToast(str))
    }
    fun navigateToSurveyScreen() = viewModelScope.launch {
        registerTransactionEvent.send(RegisterTransactionEvent.NavigateToSurveyScreen)
    }

    sealed class RegisterTransactionEvent {
        object NavigateToLoginScreen : RegisterTransactionEvent()
        data class ShowInvalidInputToast(val str:String): RegisterTransactionEvent()
        object NavigateToSurveyScreen: RegisterTransactionEvent()
    }

}