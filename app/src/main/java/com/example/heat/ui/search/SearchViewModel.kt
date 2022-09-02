package com.example.heat.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.datamodel.SearchQuery
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.util.enumerian.Cuisine
import com.example.heat.util.enumerian.DietType
import com.example.heat.util.enumerian.MealType
import com.example.heat.util.lazyDeferred

class SearchViewModel(
    private val heatRepository: HeatRepository
) : ViewModel() {


    val recipesListAtFirst by lazyDeferred {
        heatRepository.searchFoods(
            SearchQuery(
                Cuisine.NONE,
                DietType.NONE,
                "egg",
                10000,
                MealType.NONE,
                0
            )
        )
    }

    private val currentSearchQuery = MutableLiveData(
        SearchQuery(
            Cuisine.NONE, DietType.NONE, "", 1700, MealType.NONE, 0
        )
    )

    val recipesListFiltered = currentSearchQuery.switchMap { searchQuery ->
        liveData<List<FoodSummery>> {
            heatRepository.searchFoods(
                searchQuery
            )
        }
    }

    fun setCurrentSearchQuery(searchQuery: SearchQuery) {
        currentSearchQuery.postValue(searchQuery)
    }


    private val setLike = MutableLiveData(Pair(0, 0))

    fun setUserID(userId: Int, foodID: Int) {
        setLike.postValue(Pair(userId, foodID))
    }

    val likeFood by lazyDeferred {
        setLike.value?.first?.let { user ->
            setLike.value?.second?.let { food ->
                heatRepository.likeFood(user, food)
            }
        }
    }

}