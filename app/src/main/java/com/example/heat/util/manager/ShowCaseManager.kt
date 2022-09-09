package com.example.heat.util.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShowCaseManager(private val dataStore: DataStore<Preferences>) {

    //Create some keys
    companion object {
        val HOME_KEY_SHOW_CASE_GENERATE_FOOD = booleanPreferencesKey("HOME_KEY_SHOW_CASE_GENERATE_FOOD")
        val HOME_KEY_SHOW_CASE_NUTRITION_CHARTS = booleanPreferencesKey("HOME_KEY_SHOW_CASE_NUTRITION_CHARTS")
        val TRACK_FOOD_SHOW_CASE = booleanPreferencesKey("TRACK_FOOD_SHOW_CASE")
    }


    suspend fun storeHomeGenerateFoodKeyShowCase(show: Boolean) {
        dataStore.edit {
            it[HOME_KEY_SHOW_CASE_GENERATE_FOOD] = show
        }
    }
    suspend fun storeHomeNutritionChartsKeyShowCase(show: Boolean) {
        dataStore.edit {
            it[HOME_KEY_SHOW_CASE_NUTRITION_CHARTS] = show
        }
    }
    suspend fun storeTrackFoodShowCase(show: Boolean) {
        dataStore.edit {
            it[TRACK_FOOD_SHOW_CASE] = show
        }
    }

    val getHomeGenerateFoodKeyShowCase: Flow<Boolean?> = dataStore.data.map {
        it[HOME_KEY_SHOW_CASE_GENERATE_FOOD]
    }
    val getHomeNutritionChartFoodKeyShowCase: Flow<Boolean?> = dataStore.data.map {
        it[HOME_KEY_SHOW_CASE_NUTRITION_CHARTS]
    }
    val getTrackFoodKeyShowCase: Flow<Boolean?> = dataStore.data.map {
        it[TRACK_FOOD_SHOW_CASE]
    }
}