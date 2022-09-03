package com.example.heat.data.local.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.data.datamodel.user.UserPreferences

interface RoomRepository {

    //user preference
    suspend fun insertUserPreferences(userPreference :UserPreferences)
    suspend fun deleteUserPreferences(userPreference :UserPreferences)
    suspend fun deleteAllUserPreferences()
    suspend fun updateUserPreferences(userPreference :UserPreferences)
    suspend fun getUserPreference() : LiveData<UserPreferences>

    //meal
    suspend fun insertMeal(meal : FoodSummery)
    suspend fun deleteMeal(meal : FoodSummery)
    suspend fun deleteAllMeals()
    suspend fun updateMeal(meal : FoodSummery)
    suspend fun eatMeal(meal : FoodSummery, eaten:Boolean)
    suspend fun getDayMeal(day:String) : LiveData<List<FoodSummery>>
    suspend fun getWeekMeal() : LiveData<List<FoodSummery>>
    suspend fun eatMealDBSize():LiveData<Int>
    suspend fun deletePreviousRecords(day:String)
}