package com.example.heat.ui.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.ui.recipeDetail.RecipeDetailViewModel

class SurveyViewModelFactory  (
    private val recipesRepository: RecipesRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SurveyViewModel(
            recipesRepository
        ) as T
    }
}