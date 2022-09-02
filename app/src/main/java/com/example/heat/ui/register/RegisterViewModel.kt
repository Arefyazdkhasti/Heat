package com.example.heat.ui.register

import androidx.lifecycle.*
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.RegisterRequest
import com.example.heat.data.datamodel.user.UserRelatedResponse
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val heatRepository: HeatRepository,
) : ViewModel() {


    private val register = MutableLiveData(RegisterRequest("","",""))

    val registerUser = register.switchMap {  p ->
        liveData<UserRelatedResponse> {
            heatRepository.register(p)
        }
    }

    fun setRegisterUser(registerRequest: RegisterRequest) {
        register.value = registerRequest
    }

    val registerRequest by lazyDeferred {
        register.value?.let { heatRepository.register(it) }
    }

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