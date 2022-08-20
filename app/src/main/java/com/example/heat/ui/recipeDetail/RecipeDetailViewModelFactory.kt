package com.example.heat.ui.recipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository

class RecipeDetailViewModelFactory (
    private val recipeID: Int,
    private val recipesRepository: RecipesRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeDetailViewModel(
            recipeID,
            recipesRepository
        ) as T
    }
}