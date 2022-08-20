package com.example.heat.data.data.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.data.response.NetworkDataSource
import com.example.heat.data.datamodel.recipeDetail.Recipe
import com.example.heat.data.datamodel.recipeList.RecipeList

class RecipesRepositoryImpl(
    private val networkDataSource: NetworkDataSource
) : RecipesRepository {

    override suspend fun getRecipesList(offset: Int): LiveData<RecipeList> {
        fetchRecipesList(offset)
        return networkDataSource.recipesList
    }

    private suspend fun fetchRecipesList(offset: Int) {
        networkDataSource.fetchRecipesList(offset)
    }

    override suspend fun getBreakfastRecipesList(type: String, offset: Int): LiveData<RecipeList> {
        fetchBreakfastRecipesList(type, offset)
        return networkDataSource.breakfastRecipesList
    }

    private suspend fun fetchBreakfastRecipesList(type: String, offset: Int) {
        networkDataSource.fetchBreakfastRecipesList(type, offset)
    }

    override suspend fun getSnackRecipesList(type: String, offset: Int): LiveData<RecipeList> {
        fetchSnackRecipesList(type, offset)
        return networkDataSource.snackRecipesList
    }

    private suspend fun fetchSnackRecipesList(type: String, offset: Int) {
        networkDataSource.fetchSnackRecipesList(type, offset)
    }

    override suspend fun getMainCourseRecipesList(type: String, offset: Int): LiveData<RecipeList> {
        fetchMainCourseRecipesList(type, offset)
        return networkDataSource.mainCourseRecipesList
    }

    private suspend fun fetchMainCourseRecipesList(type: String, offset: Int) {
        networkDataSource.fetchMainCourseRecipesList(type, offset)
    }


    override suspend fun getRecipesDetail(id: Int): LiveData<Recipe> {
        fetchRecipeDetail(id)
        return networkDataSource.recipeDetail
    }

    private suspend fun fetchRecipeDetail(id: Int) {
        networkDataSource.fetchRecipeDetail(id)
    }

    override suspend fun searchRecipes(
        query: String,
        type: String,
        diet: String,
        offset: Int,
        number: Int
    ): LiveData<RecipeList> {
        fetchSearchRecipes(query, type, diet, offset, number)
        return networkDataSource.searchRecipe
    }

    private suspend fun fetchSearchRecipes(
        query: String,
        type: String,
        diet: String,
        offset: Int,
        number: Int
    ) {
        networkDataSource.fetchSearchRecipe(query, type, diet, offset, number)
    }
}