package com.example.heat.ui.recipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository

class RecipeDetailViewModelFactory (
    private val recipeID: Int,
    private val heatRepository: HeatRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeDetailViewModel(
            recipeID,
            heatRepository
        ) as T
    }
}