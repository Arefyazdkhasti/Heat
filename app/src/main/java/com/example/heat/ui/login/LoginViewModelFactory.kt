package com.example.heat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.data.local.repository.RoomRepository
import com.example.heat.ui.home.HomeViewModel

class LoginViewModelFactory (
    private val recipesRepository: RecipesRepository,
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            recipesRepository,
        ) as T
    }
}