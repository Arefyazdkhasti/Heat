package com.example.heat.ui.login

import androidx.lifecycle.*
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.LoginRequest
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.datamodel.user.UserRelatedResponse
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val heatRepository: HeatRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {


    //login request
    private val login = MutableLiveData(LoginRequest("", ""))

    val loginUser = login.switchMap { p ->
        liveData<UserRelatedResponse> {
            heatRepository.login(p)
        }
    }

    fun setRegisterUser(request: LoginRequest) {
        login.value = request
    }

    val loginRequest by lazyDeferred {
        login.value?.let { heatRepository.login(it) }
    }

    //get user_pref request
    private val userID = MutableLiveData(0)

    fun setUserIDLogin(id: Int) {
        userID.postValue(id)
    }

    val getUserPreferences by lazyDeferred {
        userID.value?.let {
            heatRepository.getUserPreferences(it)
        }
    }

    private val loginTransactionEvent = Channel<LoginTransactionEvent>()
    val loginEvent = loginTransactionEvent.receiveAsFlow()

    fun navigateToRegisterClicked() = viewModelScope.launch {
        loginTransactionEvent.send(LoginTransactionEvent.NavigateToRegisterScreen)
    }

    fun showInvalidInputToast(str: String) = viewModelScope.launch {
        loginTransactionEvent.send(LoginTransactionEvent.ShowInvalidInputToast(str))
    }

    fun navigateToHomeScreen() = viewModelScope.launch {
        loginTransactionEvent.send(LoginTransactionEvent.NavigateToHomeScreen)
    }

    fun saveUserPreferences(userPreferences: UserPreferences) = GlobalScope.launch {
        roomRepository.insertUserPreferences(userPreferences)
    }

    sealed class LoginTransactionEvent {
        object NavigateToRegisterScreen : LoginTransactionEvent()
        data class ShowInvalidInputToast(val str: String) : LoginTransactionEvent()
        object NavigateToHomeScreen : LoginTransactionEvent()
    }
}