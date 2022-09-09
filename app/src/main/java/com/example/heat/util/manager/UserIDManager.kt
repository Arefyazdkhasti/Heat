package com.example.heat.util.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserIDManager(private val dataStore: DataStore<Preferences>) {

    //Create some keys
    companion object {
        val USER_ID = intPreferencesKey("USER_ID")
    }

    //Store user data
    suspend fun storeUserID(id: Int) {
        dataStore.edit {
            it[USER_ID] = id
        }
    }

    val userIDFlow: Flow<Int?> = dataStore.data.map {
        it[USER_ID]
    }
}