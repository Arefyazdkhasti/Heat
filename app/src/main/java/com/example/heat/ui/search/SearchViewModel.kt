package com.example.heat.ui.search

import androidx.lifecycle.ViewModel
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.util.lazyDeferred

class SearchViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {


    val breakfastRecipesList by lazyDeferred{
        recipesRepository.getBreakfastRecipesList(BREAKFAST)
    }

    val snackRecipesList by lazyDeferred{
        recipesRepository.getSnackRecipesList(SNACK)
    }

    val mainCourseRecipesList by lazyDeferred{
        recipesRepository.getMainCourseRecipesList(MAIN_COURSE)
    }
}