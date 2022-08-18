package com.example.heat.data.data.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.recipeDetail.Recipe
import com.example.heat.data.datamodel.recipeList.RecipeList

interface RecipesRepository {

    suspend fun getRecipesList(offset: Int): LiveData<RecipeList>

    suspend fun getBreakfastRecipesList(type:String, offset: Int): LiveData<RecipeList>

    suspend fun getSnackRecipesList(type:String, offset: Int): LiveData<RecipeList>

    suspend fun getMainCourseRecipesList(type:String, offset: Int): LiveData<RecipeList>

    suspend fun getRecipesDetail(id :Int) : LiveData<Recipe>
}