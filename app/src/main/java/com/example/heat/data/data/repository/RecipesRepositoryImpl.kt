package com.example.heat.data.data.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.data.response.NetworkDataSource
import com.example.heat.data.datamodel.recipeDetail.Recipe
import com.example.heat.data.datamodel.recipeList.RecipeList

class RecipesRepositoryImpl(
    private val networkDataSource: NetworkDataSource
) : RecipesRepository {

    override suspend fun getBreakfastRecipesList(type: String): LiveData<RecipeList> {
        fetchBreakfastRecipesList(type)
        return networkDataSource.breakfastRecipesList
    }
    private suspend fun fetchBreakfastRecipesList(type:String) {
        networkDataSource.fetchBreakfastRecipesList(type)
    }

    override suspend fun getSnackRecipesList(type: String): LiveData<RecipeList> {
        fetchSnackRecipesList(type)
        return networkDataSource.snackRecipesList
    }
    private suspend fun fetchSnackRecipesList(type:String) {
        networkDataSource.fetchSnackRecipesList(type)
    }

    override suspend fun getMainCourseRecipesList(type: String): LiveData<RecipeList> {
        fetchMainCourseRecipesList(type)
        return networkDataSource.mainCourseRecipesList
    }
    private suspend fun fetchMainCourseRecipesList(type:String) {
        networkDataSource.fetchMainCourseRecipesList(type)
    }


    override suspend fun getRecipesDetail(id: Int): LiveData<Recipe> {
        fetchRecipeDetail(id)
        return networkDataSource.recipeDetail
    }
    private suspend fun fetchRecipeDetail(id : Int) {
        networkDataSource.fetchRecipeDetail(id)
    }
}