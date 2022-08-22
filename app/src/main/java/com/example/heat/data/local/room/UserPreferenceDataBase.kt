package com.example.heat.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.util.MyConvertor


@Database(entities = [UserPreferences::class], version = 1, exportSchema = false)
@TypeConverters(MyConvertor::class)
abstract class UserPreferenceDataBase(
) : RoomDatabase() {
    abstract fun userPreferenceDao(): UserPreferenceDao

    companion object {
        @Volatile
        private var instance: UserPreferenceDataBase? = null

        fun getDatabase(context: Context): UserPreferenceDataBase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, UserPreferenceDataBase::class.java, "userPreference")
                .fallbackToDestructiveMigration()
                //.addCallback(getCallback)
                .build()
    }
}
