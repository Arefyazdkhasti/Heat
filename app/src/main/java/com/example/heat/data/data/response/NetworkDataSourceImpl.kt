package com.example.heat.data.data.response

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heat.util.NoConnectivityException
import com.example.heat.data.data.RecipesApiService
import com.example.heat.data.datamodel.recipeDetail.Recipe
import com.example.heat.data.datamodel.recipeList.RecipeList

class NetworkDataSourceImpl(
    private val recipesApiService: RecipesApiService
) : NetworkDataSource {

    private val _breakfastRecipesList = MutableLiveData<RecipeList>()
    override val breakfastRecipesList: LiveData<RecipeList>
        get() = _breakfastRecipesList


    override suspend fun fetchBreakfastRecipesList(type: String) {
        try {
            val fetchRecipes = recipesApiService.getRecipesListAsync(type).await()
            _breakfastRecipesList.postValue(fetchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _snackRecipesList = MutableLiveData<RecipeList>()
    override val snackRecipesList: LiveData<RecipeList>
        get() = _snackRecipesList


    override suspend fun fetchSnackRecipesList(type: String) {
        try {
            val fetchRecipes = recipesApiService.getRecipesListAsync(type).await()
            _snackRecipesList.postValue(fetchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _mainCourseRecipesList = MutableLiveData<RecipeList>()
    override val mainCourseRecipesList: LiveData<RecipeList>
        get() = _mainCourseRecipesList


    override suspend fun fetchMainCourseRecipesList(type: String) {
        try {
            val fetchRecipes = recipesApiService.getRecipesListAsync(type).await()
            _mainCourseRecipesList.postValue(fetchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _recipeDetail = MutableLiveData<Recipe>()
    override val recipeDetail: LiveData<Recipe>
        get() = _recipeDetail


    override suspend fun fetchRecipeDetail(id: Int) {
        try {
            val fetchRecipes = recipesApiService.getRecipesDetailAsync(id).await()
            _recipeDetail.postValue(fetchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}