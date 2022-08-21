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

    private val _recipesList = MutableLiveData<RecipeList>()
    override val recipesList: LiveData<RecipeList>
        get() = _recipesList


    override suspend fun fetchRecipesList(offset: Int){
        try {
            val fetchRecipes = recipesApiService.getRecipesListAsync(null,offset).await()
            _recipesList.postValue(fetchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _breakfastRecipesList = MutableLiveData<RecipeList>()
    override val breakfastRecipesList: LiveData<RecipeList>
        get() = _breakfastRecipesList


    override suspend fun fetchBreakfastRecipesList(type: String, offset: Int) {
        try {
            val fetchRecipes = recipesApiService.getRecipesListAsync(type,offset).await()
            _breakfastRecipesList.postValue(fetchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _snackRecipesList = MutableLiveData<RecipeList>()
    override val snackRecipesList: LiveData<RecipeList>
        get() = _snackRecipesList


    override suspend fun fetchSnackRecipesList(type: String,offset: Int) {
        try {
            val fetchRecipes = recipesApiService.getRecipesListAsync(type,offset).await()
            _snackRecipesList.postValue(fetchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    private val _mainCourseRecipesList = MutableLiveData<RecipeList>()
    override val mainCourseRecipesList: LiveData<RecipeList>
        get() = _mainCourseRecipesList


    override suspend fun fetchMainCourseRecipesList(type: String,offset: Int) {
        try {
            val fetchRecipes = recipesApiService.getRecipesListAsync(type,offset).await()
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

    private val _searchRecipe = MutableLiveData<RecipeList>()
    override val searchRecipe: LiveData<RecipeList>
        get() = _searchRecipe


    override suspend fun fetchSearchRecipe(
        query: String,
        type: String,
        diet: String,
        offset: Int,
        number: Int
    ) {
        try {
            val fetchSearchRecipes = recipesApiService.searchRecipesAsync(query, type, diet, offset, number).await()
            _searchRecipe.postValue(fetchSearchRecipes)

        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}