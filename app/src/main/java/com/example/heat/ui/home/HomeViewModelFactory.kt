package com.example.heat.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.local.repository.RoomRepository

class HomeViewModelFactory (
    private val recipesRepository: RecipesRepository,
    private val roomRepository: RoomRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            recipesRepository,
            roomRepository
        ) as T
    }
}