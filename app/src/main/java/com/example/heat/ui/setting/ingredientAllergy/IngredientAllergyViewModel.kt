package com.example.heat.ui.setting.ingredientAllergy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class IngredientAllergyViewModel(
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository
) : ViewModel() {

    private val transactionsEventChannel = Channel<IngredientAllergyTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()


    fun onNextClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            IngredientAllergyTransactionsEvents.NavigateToDiseaseScreen(
                userPreference
            )
        )
    }

    fun onPreviousClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            IngredientAllergyTransactionsEvents.NavigateBackToDietTypeScreen(
                userPreference
            )
        )
    }

    fun shouldShowFillAllPartSnackBar() = viewModelScope.launch {
        transactionsEventChannel.send(IngredientAllergyTransactionsEvents.ShouldFillAllPart)
    }

    fun updateUserPreferencesToServer(userPreferences: UserPreferences) = viewModelScope.launch {
        heatRepository.saveUserPreferences(userPreferences)
    }


    sealed class IngredientAllergyTransactionsEvents() {
        object ShouldFillAllPart : IngredientAllergyTransactionsEvents()
        data class NavigateToDiseaseScreen(val userPreference: UserPreferences) : IngredientAllergyTransactionsEvents()
        data class NavigateBackToDietTypeScreen(val userPreference: UserPreferences) : IngredientAllergyTransactionsEvents()
    }
}