package com.example.heat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.data.repository.RecipesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val profileTransactionEvent = Channel<ProfileTransactionEvent>()
    val profileEvent = profileTransactionEvent.receiveAsFlow()

    fun personalDataClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToPersonalDataScreen)
    }
    fun activeLevelClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToActiveLevelScreen)
    }
    fun dietTypeClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToDietTypeToScreen)
    }
    fun abstarctGoalClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToAbstractGoalScreen)
    }
    fun ingredientAllergyClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToIngredientAllergyScreen)
    }
    fun diseaeClicked() = viewModelScope.launch {
        profileTransactionEvent.send(ProfileTransactionEvent.NavigateToDiseaseScreen)
    }

    sealed class ProfileTransactionEvent{
        object NavigateToPersonalDataScreen : ProfileTransactionEvent()
        object NavigateToActiveLevelScreen : ProfileTransactionEvent()
        object NavigateToDietTypeToScreen : ProfileTransactionEvent()
        object NavigateToAbstractGoalScreen : ProfileTransactionEvent()
        object NavigateToIngredientAllergyScreen : ProfileTransactionEvent()
        object NavigateToDiseaseScreen : ProfileTransactionEvent()
    }
}