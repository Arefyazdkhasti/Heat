package com.example.heat.data.local.room

import android.icu.util.ULocale
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.heat.data.datamodel.EatenMealItem
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.user.UserPreferences
import kotlinx.coroutines.flow.Flow
@Dao
interface EatenMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEatenMeal(meal: EatenMealItem)

    @Delete
    suspend fun deleteEatenMeal(meal:EatenMealItem)

    @Update
    suspend fun updateEatenMeal(meal: EatenMealItem)

    @Query("SELECT * FROM eaten_meal_table WHERE date = :day")
    fun getEatenDayMeals(day: String): LiveData<List<EatenMealItem>>

    @Query("SELECT * FROM eaten_meal_table")
    fun getEatenWeekMeals(): LiveData<List<EatenMealItem>>
}