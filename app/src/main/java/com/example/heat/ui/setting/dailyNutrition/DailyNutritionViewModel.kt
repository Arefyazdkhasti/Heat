package com.example.heat.ui.setting.dailyNutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DailyNutritionViewModel(
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository
) : ViewModel() {

    private val transactionsEventChannel = Channel<DailyNutritionTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()


    fun onGetStartedClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            DailyNutritionTransactionsEvents.NavigateToHomeScreen(
                userPreference
            )
        )
    }

    fun onBackClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            DailyNutritionTransactionsEvents.NavigateToProfileScreen(
                userPreference
            )
        )
    }


    sealed class DailyNutritionTransactionsEvents() {
        data class NavigateToHomeScreen(val userPreference: UserPreferences) : DailyNutritionTransactionsEvents()
        data class NavigateToProfileScreen(val userPreference: UserPreferences) : DailyNutritionTransactionsEvents()
    }
}