package com.example.heat.ui.setting.activeLevel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences

class ActiveLevelViewModelFactory (
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ActiveLevelViewModel(
            userPreferences,
            heatRepository
        ) as T
    }
}