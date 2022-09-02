package com.example.heat.ui.setting.personalData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PersonalDataViewModel(
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val transactionsEventChannel = Channel<PersonalDataTransactionsEvents>()
    val transactionEvent = transactionsEventChannel.receiveAsFlow()


    fun onNextClicked(userPreference: UserPreferences) = viewModelScope.launch {
        transactionsEventChannel.send(
            PersonalDataTransactionsEvents.NavigateToActiveLevelScreen(
                userPreference
            )
        )
    }

    fun shouldShowFillAllPartSnackBar() = viewModelScope.launch {
        transactionsEventChannel.send(PersonalDataTransactionsEvents.ShouldFillAllPart)
    }


    fun updateUserPreferences(userPreferences: UserPreferences) = viewModelScope.launch {
        roomRepository.updateUserPreferences(userPreferences)
    }

    sealed class PersonalDataTransactionsEvents() {
        object ShouldFillAllPart : PersonalDataTransactionsEvents()
        data class NavigateToActiveLevelScreen(val userPreference: UserPreferences) :
            PersonalDataTransactionsEvents()
    }
}