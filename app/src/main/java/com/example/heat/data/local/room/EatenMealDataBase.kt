package com.example.heat.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.heat.data.datamodel.EatenMealItem
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.util.MyConvertor


@Database(entities = [EatenMealItem::class], version = 1, exportSchema = false)
abstract class EatenMealDataBase(
) : RoomDatabase() {
    abstract fun mealDao(): EatenMealDao

    companion object {
        @Volatile
        private var instance: EatenMealDataBase? = null

        fun getDatabase(context: Context): EatenMealDataBase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, EatenMealDataBase::class.java, "eatenMeals.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
