package com.example.heat.ui.setting.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.ui.setting.ingredientAllergy.IngredientAllergyViewModel

class DiseaseViewModelFactory (
    private val userPreferences: UserPreferences?,
    private val recipesRepository: RecipesRepository,
    private val roomRepository: RoomRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiseaseViewModel(
            userPreferences,
            recipesRepository,
            roomRepository
        ) as T
    }
}