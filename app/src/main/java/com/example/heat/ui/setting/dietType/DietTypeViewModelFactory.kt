package com.example.heat.ui.setting.dietType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.user.UserPreferences

class DietTypeViewModelFactory  (
    private val userPreferences: UserPreferences?,
    private val heatRepository: HeatRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DietTypeViewModel(
            userPreferences,
            heatRepository
        ) as T
    }
}