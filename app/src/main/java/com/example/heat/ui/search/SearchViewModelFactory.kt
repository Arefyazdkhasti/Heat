package com.example.heat.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository

class SearchViewModelFactory (
    private val recipesRepository: RecipesRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            recipesRepository
        ) as T
    }
}