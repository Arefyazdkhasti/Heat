package com.example.heat.ui.trackFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository

class TrackFoodsViewModelFactory (
    private val recipesRepository: RecipesRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackFoodsViewModel(
            recipesRepository
        ) as T
    }
}