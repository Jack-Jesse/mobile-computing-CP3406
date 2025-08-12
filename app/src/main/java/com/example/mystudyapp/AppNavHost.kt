package com.example.mystudyapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystudyapp.home.HomeScreen
import com.example.mystudyapp.mediaupload.MediaUploadPage
import com.example.mystudyapp.signin.SignInScreen

object Screen {
    const val SIGN_IN = "signIn"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val MEDIA_UPLOAD = "mediaUpload"
    const val EDIT_PROFILE = "editProfile"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SIGN_IN
    ) {
        appNavGraph(navController)
    }
}

