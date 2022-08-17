package com.example.heat.ui.recipeDetail

import androidx.lifecycle.ViewModel
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.util.lazyDeferred

class RecipeDetailViewModel(
    private val recipeID: Int,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    val recipeDetail by lazyDeferred{
        recipesRepository.getRecipesDetail(recipeID)
    }


    sealed class RecipeDetailEventListener {
        data class LikeRecipe(val recipeID: Int) : RecipeDetailEventListener()
    }
}