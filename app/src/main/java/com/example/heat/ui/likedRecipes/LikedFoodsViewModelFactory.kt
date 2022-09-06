package com.example.heat.ui.likedRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.util.ErrorHandling

class LikedFoodsViewModelFactory(
    private val errorHandling: ErrorHandling,
    private val heatRepository: HeatRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LikedFoodsViewModel(
            errorHandling,
            heatRepository
        ) as T
    }
}