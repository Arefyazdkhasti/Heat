package com.example.heat.ui.setting.dietType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DietTypeViewModel (
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository
) : ViewModel() {

    private val transactionsEventChannel = Channel<DietTypeTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()


    fun onNextClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            DietTypeTransactionsEvents.NavigateToIngredientAllergyScreen(
                userPreference
            )
        )
    }

    fun onPreviousClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            DietTypeTransactionsEvents.NavigateBackToAbstractGoalScreen(
                userPreference
            )
        )
    }

    fun shouldShowFillAllPartSnackBar() = viewModelScope.launch {
        transactionsEventChannel.send(DietTypeTransactionsEvents.ShouldFillAllPart)
    }

    sealed class DietTypeTransactionsEvents() {
        object ShouldFillAllPart : DietTypeTransactionsEvents()
        data class NavigateToIngredientAllergyScreen(val userPreference: UserPreferences) : DietTypeTransactionsEvents()
        data class NavigateBackToAbstractGoalScreen(val userPreference: UserPreferences) : DietTypeTransactionsEvents()
    }
}