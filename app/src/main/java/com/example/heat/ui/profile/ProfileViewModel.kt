package com.example.heat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.local.repository.RoomRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.heat.util.lazyDeferred

class ProfileViewModel(
    private val heatRepository: HeatRepository,
    private val roomRepository: RoomRepository

) : ViewModel() {

    private val profileTransactionEvent = Channel<ProfileTransactionEvent>()
    val profileEvent = profileTransactionEvent.receiveAsFlow()

    fun settingClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToSettingScreen)
    }
    fun personalDataClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToPersonalDataScreen)
    }
    fun activeLevelClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToActiveLevelScreen)
    }
    fun dietTypeClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToDietTypeToScreen)
    }
    fun abstractGoalClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToAbstractGoalScreen)
    }
    fun ingredientAllergyClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToIngredientAllergyScreen)
    }
    fun diseaseClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToDiseaseScreen)
    }
    fun logoutClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToLoginScreen)
    }
    fun dailyNutritionClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToDailyNutritionScreen)
    }
    fun likedFoodsClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToLikedFoodsScreen)
    }
    val getUserPreference by lazyDeferred{
        roomRepository.getUserPreference()
    }

    sealed class ProfileTransactionEvent{
        object NavigateToSettingScreen : ProfileTransactionEvent()
        object NavigateToPersonalDataScreen : ProfileTransactionEvent()
        object NavigateToActiveLevelScreen : ProfileTransactionEvent()
        object NavigateToDietTypeToScreen : ProfileTransactionEvent()
        object NavigateToAbstractGoalScreen : ProfileTransactionEvent()
        object NavigateToIngredientAllergyScreen : ProfileTransactionEvent()
        object NavigateToDiseaseScreen : ProfileTransactionEvent()
        object NavigateToLoginScreen : ProfileTransactionEvent()
        object NavigateToDailyNutritionScreen : ProfileTransactionEvent()
        object NavigateToLikedFoodsScreen : ProfileTransactionEvent()
    }
}