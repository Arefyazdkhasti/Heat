package com.example.heat.ui.setting.disease

import androidx.lifecycle.*
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.util.enumerian.*
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DiseaseViewModel(
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository,
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

    private val userPref = MutableLiveData(
        UserPreferences(
            0,
            "",
            0.0,
            0.0,
            0,
            Gender.MALE,
            ActiveLevel.NONE,
            AbstractGoal.NONE,
            UserDietType.ANY_THING,
            arrayListOf(),
            arrayListOf()
        )
    )

    val userPreference = userPref.switchMap { p ->
        liveData<UserPreferences> {
            heatRepository.saveUserPreferences(p)
        }
    }

    fun setCurrentUserProf(userPreference: UserPreferences) {
        userPref.value = userPreference
    }

    val sendUserPreferenceRequest by lazyDeferred{
        userPref.value?.let { heatRepository.saveUserPreferences(it) }
    }

    sealed class DiseaseTransactionsEvents() {
        object ShouldFillAllPart : DiseaseTransactionsEvents()
        data class NavigateToHomeScreen(val userPreference: UserPreferences) :
            DiseaseTransactionsEvents()

        data class NavigateBackToIngredientAllergyScreen(val userPreference: UserPreferences) :
            DiseaseTransactionsEvents()
    }
}