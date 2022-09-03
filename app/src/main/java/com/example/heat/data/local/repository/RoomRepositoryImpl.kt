package com.example.heat.data.local.repository

import androidx.lifecycle.LiveData
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.data.local.room.MealDataBase
import com.example.heat.data.local.room.UserPreferenceDataBase

class RoomRepositoryImpl(
    private val userPreferenceDataBase: UserPreferenceDataBase,
    private val mealDataBase: MealDataBase,
) : RoomRepository {
    override suspend fun insertUserPreferences(userPreference: UserPreferences) =
        userPreferenceDataBase.userPreferenceDao().insertUserPreferences(userPreference)

    override suspend fun deleteUserPreferences(userPreference: UserPreferences) =
        userPreferenceDataBase.userPreferenceDao().deleteUserPreferences(userPreference)

    override suspend fun deleteAllUserPreferences() = userPreferenceDataBase.userPreferenceDao().deleteAllUserPreferences()

    override suspend fun updateUserPreferences(userPreference: UserPreferences) =
        userPreferenceDataBase.userPreferenceDao().updateUserPreferences(userPreference)

    override suspend fun getUserPreference(): LiveData<UserPreferences> =
        userPreferenceDataBase.userPreferenceDao().getUserPreference()

    override suspend fun userPreferenceSize(): LiveData<Int> = userPreferenceDataBase.userPreferenceDao().getUserPreferenceSize()

    override suspend fun insertMeal(meal: FoodSummery) = mealDataBase.mealDao().insertMeal(meal)

    override suspend fun deleteMeal(meal: FoodSummery) = mealDataBase.mealDao().deleteMeal(meal)

    override suspend fun deleteAllMeals()  = mealDataBase.mealDao().deleteAllMeal()

    override suspend fun updateMeal(meal: FoodSummery) = mealDataBase.mealDao().updateMeal(meal)

    override suspend fun eatMeal(meal: FoodSummery, eaten: Boolean) =
        mealDataBase.mealDao().eatMeal(meal.id, eaten)

    override suspend fun getDayMeal(day:String): LiveData<List<FoodSummery>> =
        mealDataBase.mealDao().getDayMeals(day)

    override suspend fun getWeekMeal(): LiveData<List<FoodSummery>>  =
        mealDataBase.mealDao().getWeekMeals()

    override suspend fun eatMealDBSize(): LiveData<Int> = mealDataBase.mealDao().getMealSize()

    override suspend fun deletePreviousRecords(day: String) = mealDataBase.mealDao().deletePreviousRecords(day)

}