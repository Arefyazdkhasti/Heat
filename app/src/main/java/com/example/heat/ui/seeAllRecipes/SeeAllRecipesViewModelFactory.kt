package com.example.heat.ui.seeAllRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository

class SeeAllRecipesViewModelFactory(
    private val type: String,
    private val heatRepository: HeatRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeeAllRecipesViewModel(
            type,
            heatRepository
        ) as T
    }
}