package com.example.heat.ui.setting.dietType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.ui.setting.abstractGoal.AbstractGoalViewModel

class DietTypeViewModelFactory  (
    private val userPreferences: UserPreferences?,
    private val recipesRepository: RecipesRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DietTypeViewModel(
            userPreferences,
            recipesRepository
        ) as T
    }
}