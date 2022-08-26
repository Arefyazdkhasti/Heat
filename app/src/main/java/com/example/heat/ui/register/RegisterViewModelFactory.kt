package com.example.heat.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.data.repository.RecipesRepository
import com.example.heat.ui.login.LoginViewModel

class RegisterViewModelFactory (
    private val recipesRepository: RecipesRepository,
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(
            recipesRepository,
        ) as T
    }
}