package com.example.mystudyapp.signin

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystudyapp.data.local.UserDataStore.Companion.USERNAME_KEY
import com.example.mystudyapp.data.local.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlin.math.log


val USERNAME_KEY = stringPreferencesKey("username")
val Context.dataStore by preferencesDataStore(name = "user_prefs")



fun saveUsername(context: Context, username: String) {
    CoroutineScope(Dispatchers.IO).launch {
        context.dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }
}

@Composable
fun getUsername(context: Context) = context.dataStore.data
    .map { prefs ->
        prefs[USERNAME_KEY] ?: ""
    }





@Composable
fun SignInScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Welcome to My Study App!\nSign in to continue")
            UsernameInput(LocalContext.current, navController)
            Button(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Text("Skip Sign In")
            }
        }
    }
}

@Composable
fun UsernameInput(context: Context, navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    TextField(
        value = username,
        onValueChange = { newText -> username = newText },
        label = { Text("Enter username") }
    )
    Button(onClick = {
        saveUsername(context, username)
        println("Username saved: $username")
        navController.navigate("home")


    }) {
        Text("Save")
    }
}