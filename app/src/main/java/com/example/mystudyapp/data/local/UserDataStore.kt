package com.example.mystudyapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore




val Context.dataStore by preferencesDataStore(name = "user_prefs")



class UserDataStore(private val context: Context) {
    companion object {
        val USERNAME_KEY = stringPreferencesKey("username")
    }
}