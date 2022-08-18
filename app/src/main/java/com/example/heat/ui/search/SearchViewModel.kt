package com.example.heat.ui.search

import androidx.lifecycle.ViewModel
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.util.lazyDeferred

class SearchViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {


    val breakfastRecipesList by lazyDeferred{
        recipesRepository.getBreakfastRecipesList(BREAKFAST,0)
    }

    val snackRecipesList by lazyDeferred{
        recipesRepository.getSnackRecipesList(SNACK,0)
    }

    val mainCourseRecipesList by lazyDeferred{
        recipesRepository.getMainCourseRecipesList(MAIN_COURSE,0)
    }
}