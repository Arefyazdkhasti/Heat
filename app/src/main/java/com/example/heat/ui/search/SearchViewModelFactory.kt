package com.example.heat.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.util.ErrorHandling

class SearchViewModelFactory (
    private val errorHandling: ErrorHandling,
    private val heatRepository: HeatRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            errorHandling,
            heatRepository
        ) as T
    }
}