package com.example.heat.ui.seeAllRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository

class SeeAllRecipesViewModelFactory(
    private val type: String,
    private val recipesRepository: RecipesRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeeAllRecipesViewModel(
            type,
            recipesRepository
        ) as T
    }
}