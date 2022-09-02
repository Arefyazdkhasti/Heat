package com.example.heat.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heat.data.network.repository.HeatRepository
import com.example.heat.data.local.repository.RoomRepository

class ProfileViewModelFactory(
    private val heatRepository: HeatRepository,
    private val roomRepository: RoomRepository,
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            heatRepository,
            roomRepository
        ) as T
    }
}