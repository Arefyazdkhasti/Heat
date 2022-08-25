package com.example.heat.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.datamodel.SearchQuery
import com.example.heat.data.datamodel.recipeList.RecipeList
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.MealType
import com.example.heat.util.lazyDeferred

class SearchViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {


    val recipesListAtFirst by lazyDeferred {
        recipesRepository.searchRecipes("", "", "", 0, 100)
    }

    private val currentSearchQuery = MutableLiveData(
        SearchQuery(
            "", DietType.ANY_THING, MealType.all, 0, 10000
        )
    )


    val recipesListFiltered = currentSearchQuery.switchMap { searchQuery ->
        liveData<RecipeList> {
            recipesRepository.searchRecipes(
                searchQuery.query,
                searchQuery.mealType.toString(),
                searchQuery.dietType.toString(),
                0,
                100
            )
        }
    }

    fun setCurrentSearchQuery(searchQuery: SearchQuery) {
        currentSearchQuery.postValue(searchQuery)
    }
}