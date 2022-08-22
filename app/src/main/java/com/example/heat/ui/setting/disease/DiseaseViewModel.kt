package com.example.heat.ui.setting.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DiseaseViewModel (
    private val userPreferences: UserPreferences?,
    private val recipesRepository: RecipesRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val transactionsEventChannel = Channel<DiseaseTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()


    fun onNextClicked(userPreference: UserPreferences) = viewModelScope.launch {

        transactionsEventChannel.send(
            DiseaseTransactionsEvents.NavigateToHomeScreen(
                userPreference
            )
        )
    }

    fun onPreviousClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            DiseaseTransactionsEvents.NavigateBackToIngredientAllergyScreen(
                userPreference
            )
        )
    }

    fun shouldShowFillAllPartSnackBar() = viewModelScope.launch {
        transactionsEventChannel.send(DiseaseTransactionsEvents.ShouldFillAllPart)
    }

    fun saveUserPreferences(userPreferences: UserPreferences) = viewModelScope.launch {
        roomRepository.insertUserPreferences(userPreferences)
    }

    sealed class DiseaseTransactionsEvents() {
        object ShouldFillAllPart : DiseaseTransactionsEvents()
        data class NavigateToHomeScreen(val userPreference: UserPreferences) : DiseaseTransactionsEvents()
        data class NavigateBackToIngredientAllergyScreen(val userPreference: UserPreferences) : DiseaseTransactionsEvents()
    }
}