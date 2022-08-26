package com.example.heat.data.local.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.EatenMealItem
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.user.UserPreferences

interface RoomRepository {

    //user preference
    suspend fun insertUserPreferences(userPreference :UserPreferences)
    suspend fun deleteUserPreferences(userPreference :UserPreferences)
    suspend fun updateUserPreferences(userPreference :UserPreferences)
    suspend fun getUserPreference() : LiveData<UserPreferences>

    //meal
    suspend fun insertMeal(meal : MealListItem)
    suspend fun deleteMeal(meal : MealListItem)
    suspend fun updateMeal(meal : MealListItem)
    suspend fun eatMeal(meal : MealListItem, eaten:Boolean)
    suspend fun getDayMeal() : LiveData<List<MealListItem>>
    suspend fun getWeekMeal() : LiveData<List<MealListItem>>
    suspend fun eatMealDBSize():LiveData<Int>
}