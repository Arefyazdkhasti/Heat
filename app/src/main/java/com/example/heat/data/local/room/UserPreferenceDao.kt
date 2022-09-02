package com.example.heat.data.local.room

import android.icu.util.ULocale
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.flow.Flow
@Dao
interface UserPreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(userPreference: UserPreferences)

    @Delete
    suspend fun deleteUserPreferences(userPreference: UserPreferences)

    @Update
    suspend fun updateUserPreferences(userPreference: UserPreferences)

    @Query("SELECT * FROM userPreference_table")
    fun getUserPreference(): LiveData<UserPreferences>

    @Query("DELETE FROM userPreference_table")
    fun deleteAllUserPreferences()
}