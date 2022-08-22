package com.example.heat.data.local.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.data.response.NetworkDataSource
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.room.UserPreferenceDataBase

class RoomRepositoryImpl(
    private val userPreferenceDataBase: UserPreferenceDataBase
) : RoomRepository {
    override suspend fun insertUserPreferences(userPreference: UserPreferences) = userPreferenceDataBase.userPreferenceDao().insertUserPreferences(userPreference)

    override suspend fun deleteUserPreferences(userPreference: UserPreferences) = userPreferenceDataBase.userPreferenceDao().deleteUserPreferences(userPreference)

    override suspend fun updateUserPreferences(userPreference: UserPreferences) = userPreferenceDataBase.userPreferenceDao().updateUserPreferences(userPreference)

    override suspend fun getUserPreference(): LiveData<UserPreferences> = userPreferenceDataBase.userPreferenceDao().getUserPreference()
}