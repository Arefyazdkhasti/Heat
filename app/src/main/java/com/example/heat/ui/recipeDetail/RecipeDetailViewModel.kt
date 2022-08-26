package com.example.heat.ui.recipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.util.lazyDeferred
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val recipeID: Int,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    val recipeDetail by lazyDeferred {
        recipesRepository.getRecipesDetail(recipeID)
    }

    fun likeAndUnlikeRecipe(recipeID: Int, userID: Int) = viewModelScope.launch {
        //roomRepository.updateUserPreferences(userPreferences)

    }

    sealed class RecipeDetailEventListener {
        data class LikeAndUnlikeRecipe(val recipeID: Int, val userID: Int) : RecipeDetailEventListener()
    }
}