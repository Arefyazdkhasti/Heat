package com.example.heat.data.local.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.user.UserPreferences

interface RoomRepository {
    suspend fun insertUserPreferences(userPreference :UserPreferences)

    suspend fun deleteUserPreferences(userPreference :UserPreferences)

    suspend fun updateUserPreferences(userPreference :UserPreferences)

    suspend fun getUserPreference() : LiveData<UserPreferences>
}