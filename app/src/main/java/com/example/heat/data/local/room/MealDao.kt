package com.example.heat.data.local.room

import android.icu.util.ULocale
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.flow.Flow
@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal:MealListItem)

    @Delete
    suspend fun deleteMeal(meal:MealListItem)

    @Update
    suspend fun updateMeal(meal: MealListItem)

    @Query ("UPDATE mealList_table SET eaten = :eaten WHERE id = :mealID")
    fun eatMeal(mealID:Int, eaten: Boolean)

    @Query("SELECT * FROM mealList_table WHERE date = :day")
    fun getDayMeals(day: String): LiveData<List<MealListItem>>

    @Query("SELECT * FROM mealList_table")
    fun getWeekMeals(): LiveData<List<MealListItem>>
}