package com.example.heat.data.data.response

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.recipeDetail.Recipe
import com.example.heat.data.datamodel.recipeList.RecipeList

interface NetworkDataSource {

    //Recipes
    val breakfastRecipesList: LiveData<RecipeList>
    suspend fun fetchBreakfastRecipesList(type:String)

    val snackRecipesList: LiveData<RecipeList>
    suspend fun fetchSnackRecipesList(type:String)

    val mainCourseRecipesList: LiveData<RecipeList>
    suspend fun fetchMainCourseRecipesList(type:String)

    val recipeDetail : LiveData<Recipe>
    suspend fun fetchRecipeDetail(id :Int)
}