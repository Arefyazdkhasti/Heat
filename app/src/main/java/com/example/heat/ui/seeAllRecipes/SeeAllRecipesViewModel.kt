package com.example.heat.ui.seeAllRecipes

import androidx.lifecycle.*
import com.example.heat.data.network.repository.HeatRepository

class SeeAllRecipesViewModel (
    private val type: String,
    private val heatRepository: HeatRepository
) : ViewModel() {


   /* val recipe by lazyDeferred {
        returnApi()
    }

    private suspend fun returnApi(): LiveData<RecipeList> {
        return when (type) {
            BREAKFAST -> recipesRepository.getBreakfastRecipesList(BREAKFAST,0)
            MAIN_COURSE -> recipesRepository.getMainCourseRecipesList(MAIN_COURSE,0)
            SNACK -> recipesRepository.getSnackRecipesList(SNACK,0)
            else -> recipesRepository.getRecipesList(0)
        }
    }

    private val currentPage = MutableLiveData(1)

    val recipePage = currentPage.switchMap {  p ->
        liveData<RecipeList> {
            returnApi(p)
        }
    }

    fun setCurrentPage(query: Int) {
        currentPage.value = query
    }

    private suspend fun returnApi(offset:Int): LiveData<RecipeList> {
        return when (type) {
            BREAKFAST -> recipesRepository.getBreakfastRecipesList(BREAKFAST,offset)
            MAIN_COURSE -> recipesRepository.getMainCourseRecipesList(MAIN_COURSE,offset)
            SNACK -> recipesRepository.getSnackRecipesList(SNACK,offset)
            else -> recipesRepository.getRecipesList(offset)
        }
    }*/
}