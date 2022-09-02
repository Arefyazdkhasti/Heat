package com.example.heat.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.heat.data.datamodel.food.foodSummery.FoodSummery
@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeal(meal:FoodSummery)

    @Delete
    suspend fun deleteMeal(meal:FoodSummery)

    @Query("DELETE FROM foodSummery_list_table")
    fun deleteAllMeal()

    @Update
    suspend fun updateMeal(meal: FoodSummery)

    @Query ("UPDATE foodSummery_list_table SET eaten = :eaten WHERE id = :mealID")
    fun eatMeal(mealID:Int, eaten: Boolean)

    @Query("SELECT * FROM foodSummery_list_table WHERE localDate = :day")
    fun getDayMeals(day:String): LiveData<List<FoodSummery>>

    @Query("SELECT * FROM foodSummery_list_table ")
    fun getWeekMeals(): LiveData<List<FoodSummery>>


    @Query("SELECT count(*) FROM foodSummery_list_table")
    fun getMealSize(): LiveData<Int>
}