package com.example.heat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.local.repository.RoomRepository

class ProfileViewModelFactory(
    private val recipesRepository: RecipesRepository,
    private val roomRepository: RoomRepository,
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            recipesRepository,
            roomRepository
        ) as T
    }
}