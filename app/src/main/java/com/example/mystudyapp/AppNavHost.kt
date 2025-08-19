package com.example.mystudyapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit

) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.SIGN_IN
    ) {

        appNavGraph(
            navController = navController,
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated
        )
    }
}