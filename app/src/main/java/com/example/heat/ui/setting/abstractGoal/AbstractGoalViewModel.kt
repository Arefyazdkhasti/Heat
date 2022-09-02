package com.example.heat.ui.setting.abstractGoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AbstractGoalViewModel (
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository
) : ViewModel() {

    private val transactionsEventChannel = Channel<AbstractGoalTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()


    fun onNextClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            AbstractGoalTransactionsEvents.NavigateToDietTypeScreen(
                userPreference
            )
        )
    }

    fun onPreviousClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            AbstractGoalTransactionsEvents.NavigateBackToActiveLevelScreen(
                userPreference
            )
        )
    }

    fun shouldShowFillAllPartSnackBar() = viewModelScope.launch {
        transactionsEventChannel.send(AbstractGoalTransactionsEvents.ShouldFillAllPart)
    }

    sealed class AbstractGoalTransactionsEvents() {
        object ShouldFillAllPart : AbstractGoalTransactionsEvents()
        data class NavigateToDietTypeScreen(val userPreference: UserPreferences) : AbstractGoalTransactionsEvents()
        data class NavigateBackToActiveLevelScreen(val userPreference: UserPreferences) : AbstractGoalTransactionsEvents()
    }
}