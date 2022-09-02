package com.example.heat.ui.setting.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository

class DiseaseViewModelFactory (
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository,
    private val roomRepository: RoomRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiseaseViewModel(
            userPreferences,
            heatRepository,
            roomRepository
        ) as T
    }
}