package com.example.heat.data.local.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.data.response.NetworkDataSource
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.room.MealDataBase
import com.example.heat.data.local.room.UserPreferenceDataBase

class RoomRepositoryImpl(
    private val userPreferenceDataBase: UserPreferenceDataBase,
    private val mealDataBase: MealDataBase
) : RoomRepository {
    override suspend fun insertUserPreferences(userPreference: UserPreferences) = userPreferenceDataBase.userPreferenceDao().insertUserPreferences(userPreference)

    override suspend fun deleteUserPreferences(userPreference: UserPreferences) = userPreferenceDataBase.userPreferenceDao().deleteUserPreferences(userPreference)

    override suspend fun updateUserPreferences(userPreference: UserPreferences) = userPreferenceDataBase.userPreferenceDao().updateUserPreferences(userPreference)

    override suspend fun getUserPreference(): LiveData<UserPreferences> = userPreferenceDataBase.userPreferenceDao().getUserPreference()

    override suspend fun insertMeal(meal: MealListItem) = mealDataBase.mealDao().insertMeal(meal)

    override suspend fun deleteMeal(meal: MealListItem) = mealDataBase.mealDao().deleteMeal(meal)

    override suspend fun updateMeal(meal: MealListItem)= mealDataBase.mealDao().updateMeal(meal)

    override suspend fun getDayMeal(day: String): LiveData<List<MealListItem>> = mealDataBase.mealDao().getDayMeals(day)

    override suspend fun getWeekMeal(): LiveData<List<MealListItem>> = mealDataBase.mealDao().getWeekMeals()
}