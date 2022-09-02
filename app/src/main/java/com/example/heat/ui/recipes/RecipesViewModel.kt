package com.example.heat.ui.recipes

import androidx.lifecycle.ViewModel
import com.example.heat.data.network.repository.HeatRepository

class RecipesViewModel(
    private val heatRepository: HeatRepository
) : ViewModel() {


    /*val allRecipeList by lazyDeferred{
        recipesRepository.getRecipesList(0)
    }

    val breakfastRecipesList by lazyDeferred{
        recipesRepository.getBreakfastRecipesList(BREAKFAST,0)
    }

    val snackRecipesList by lazyDeferred{
        recipesRepository.getSnackRecipesList(SNACK,0)
    }

    val mainCourseRecipesList by lazyDeferred{
        recipesRepository.getMainCourseRecipesList(MAIN_COURSE,0)
    }*/
}