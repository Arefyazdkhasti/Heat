package com.example.heat.data.data.response

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.recipeDetail.Recipe
import com.example.heat.data.datamodel.recipeList.RecipeList

interface NetworkDataSource {

    //Recipes
    val recipesList: LiveData<RecipeList>
    suspend fun fetchRecipesList(offset: Int)

    val breakfastRecipesList: LiveData<RecipeList>
    suspend fun fetchBreakfastRecipesList(type:String, offset: Int)

    val snackRecipesList: LiveData<RecipeList>
    suspend fun fetchSnackRecipesList(type:String, offset: Int)

    val mainCourseRecipesList: LiveData<RecipeList>
    suspend fun fetchMainCourseRecipesList(type:String, offset: Int)

    val recipeDetail : LiveData<Recipe>
    suspend fun fetchRecipeDetail(id :Int)


    //search
    val searchRecipe: LiveData<RecipeList>
    suspend fun fetchSearchRecipe(query:String, type:String, diet:String, offset: Int, number: Int)
}