package com.example.heat.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.heat.data.datamodel.MealListItem
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.util.MyConvertor


@Database(entities = [MealListItem::class], version = 1, exportSchema = false)
abstract class MealDataBase(
) : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var instance: MealDataBase? = null

        fun getDatabase(context: Context): MealDataBase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, MealDataBase::class.java, "mealList")
                .fallbackToDestructiveMigration()
                .build()
    }
}
