package com.example.mystudyapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost( // This is around line 23 according to your new stack trace
        navController = navController,
        startDestination = Screen.SIGN_IN // Uses Screen from AppNavGraph.kt
    ) {
        // This appNavGraph function MUST define composable(Screen.SIGN_IN) { ... }
        appNavGraph(navController)
    }
}