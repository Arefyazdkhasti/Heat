package com.example.heat.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(private val dataStore: DataStore<Preferences>) {

    //Create some keys
    companion object {
        val IS_USER_LOGGED_IN = booleanPreferencesKey("USER_IS_LOGIN")
    }

    //Store user data
    suspend fun storeUser(isLoggedIn: Boolean) {
        dataStore.edit {
            it[IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    val userIsLoggedInFlow: Flow<Boolean?> = dataStore.data.map {
        it[IS_USER_LOGGED_IN]
    }
}