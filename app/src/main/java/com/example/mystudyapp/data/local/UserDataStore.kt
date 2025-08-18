package com.example.mystudyapp.data.local

import java.util.prefs.Preferences
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey




val Context.dataStore by preferencesDataStore(name = "user_prefs")



class UserDataStore(private val context: Context) {
    companion object {
        val USERNAME_KEY = stringPreferencesKey("username")
    }
}