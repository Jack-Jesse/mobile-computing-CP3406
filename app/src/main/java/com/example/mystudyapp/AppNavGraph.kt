package com.example.mystudyapp

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mystudyapp.home.HomeScreen
import com.example.mystudyapp.mediaupload.MediaUploadPage
import com.example.mystudyapp.mediaupload.convertPdfToTextAndGenerateFlashcards
import com.example.mystudyapp.profile.ProfileScreen
import com.example.mystudyapp.settings.SettingsScreen
import com.example.mystudyapp.signin.SignInScreen


object Screen {
    const val SIGN_IN = "signIn"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val MEDIA_UPLOAD = "mediaUpload"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.appNavGraph(
    navController: NavHostController,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {
    composable(Screen.SIGN_IN) {
        SignInScreen(navController = navController)
    }

    // Other screens
    composable(Screen.HOME) {
        HomeScreen(
            navController = navController,
            context = LocalContext.current
        )
    }

    // Add new route for SettingsScreen:
    composable(Screen.SETTINGS) {
        SettingsScreen(
            navController = navController,
            isDarkTheme = darkTheme,
            onThemeToggle = onThemeUpdated
        )    }

    composable(Screen.PROFILE) {
        ProfileScreen(navController = navController)
    }


    composable(Screen.MEDIA_UPLOAD) {
        val context = LocalContext.current

        var flashcards by remember { mutableStateOf<List<String>>(emptyList()) }
        var showFlashcardDialog by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var eventScheduledMessage by remember { mutableStateOf<String?>(null) }
        var eventName by remember { mutableStateOf("") }


        // Launcher for picking a PDF
        val pickPdfLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                isLoading = true
                eventScheduledMessage = null
                convertPdfToTextAndGenerateFlashcards(
                    context = context,
                    fileUri = uri,
                    onFlashcardsReady = { generatedFlashcards ->
                        isLoading = false
                        if (generatedFlashcards.isNotEmpty()) {
                            flashcards = generatedFlashcards
                            showFlashcardDialog = true
                        }
                    },
                    onError = { errorMsg ->
                        isLoading = false
                    }
                )
            }
        }


        // call to MediaUploadPage
        MediaUploadPage(
            navController = navController,
            onPickUploadMedia = {
                pickPdfLauncher.launch("application/pdf")
            },
            flashcards = flashcards,
            showFlashcardDialog = showFlashcardDialog,
            isLoading = isLoading,
            onDismissFlashcards = { showFlashcardDialog = false },

            eventName = eventName,
            onEventNameChanged = { newName ->
                eventName = newName
            },
            onEventScheduled = { message ->
                eventScheduledMessage = message
            },
            eventScheduledMessage = eventScheduledMessage,
            onDismissEventScheduledMessage = { eventScheduledMessage = null }
        )
    }
}