package com.example.heat.ui.setting.activeLevel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ActiveLevelViewModel (
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository
) : ViewModel() {

    private val transactionsEventChannel = Channel<ActiveLevelTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()


    fun onNextClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            ActiveLevelTransactionsEvents.NavigateToAbstractGoalScreen(
                userPreference
            )
        )
    }

    fun onPreviousClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            ActiveLevelTransactionsEvents.NavigateBackToPersonalDataScreen(
                userPreference
            )
        )
    }

    fun shouldShowFillAllPartSnackBar() = viewModelScope.launch {
        transactionsEventChannel.send(ActiveLevelTransactionsEvents.ShouldFillAllPart)
    }

    fun updateUserPreferencesToServer(userPreferences: UserPreferences) = viewModelScope.launch {
        heatRepository.saveUserPreferences(userPreferences)
    }

    sealed class ActiveLevelTransactionsEvents() {
        object ShouldFillAllPart : ActiveLevelTransactionsEvents()
        data class NavigateToAbstractGoalScreen(val userPreference: UserPreferences) : ActiveLevelTransactionsEvents()
        data class NavigateBackToPersonalDataScreen(val userPreference: UserPreferences) : ActiveLevelTransactionsEvents()
    }
}