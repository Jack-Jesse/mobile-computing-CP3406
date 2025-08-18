package com.example.mystudyapp // ENSURE THIS PACKAGE IS THE SAME AS AppNavHost.kt

// ... other necessary imports ...
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
import com.example.mystudyapp.settings.SettingsScreen // Ensure this is the correct import for your SettingsScreen
import com.example.mystudyapp.signin.SignInScreen // Make sure this import is correct


object Screen {
    const val SIGN_IN = "signIn"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val MEDIA_UPLOAD = "mediaUpload"
    const val EDIT_PROFILE = "editProfile"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.appNavGraph(
    navController: NavHostController,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {
    composable(Screen.SIGN_IN) { // <<< Uses the Screen.SIGN_IN ("signIn") from above
        SignInScreen(navController = navController) // Your actual SignInScreen composable
    }

    // Other screens
    composable(Screen.HOME) {
        HomeScreen(
            navController = navController,
            context = LocalContext.current
        )
    }

    // Add the new route for SettingsScreen:
    composable(Screen.SETTINGS) {
        SettingsScreen(
            navController = navController,
            isDarkTheme = darkTheme,    // <--- PASS IT HERE
            onThemeToggle = onThemeUpdated // <--- AND PASS THIS HERE
        )    }

    composable(Screen.PROFILE) {
        ProfileScreen(navController = navController)
    }


    composable(Screen.MEDIA_UPLOAD) {
        val context = LocalContext.current

        var flashcards by remember { mutableStateOf<List<String>>(emptyList()) }
        var showFlashcardDialog by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var eventScheduledMessage by remember { mutableStateOf<String?>(null) }
        var eventName by remember { mutableStateOf("") }


        // Launcher for picking a PDF
        val pickPdfLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                isLoading = true
                errorMessage = null
                eventScheduledMessage = null
                convertPdfToTextAndGenerateFlashcards(
                    context = context,
                    fileUri = uri,
                    onFlashcardsReady = { generatedFlashcards ->
                        isLoading = false
                        if (generatedFlashcards.isNotEmpty()) {
                            flashcards = generatedFlashcards
//                            currentFlashcardIndex = 0 // RESET HERE
                            showFlashcardDialog = true
                        } else {                            errorMessage = "No flashcards could be generated from the PDF."
                        }
                    },
                    onError = { errorMsg ->
                        isLoading = false
                        errorMessage = errorMsg
                    }
                )
            }
        }


        // This is the call to MediaUploadPage, around line 103 in your file
        MediaUploadPage(
            navController = navController,
            onPickUploadMedia = {
                pickPdfLauncher.launch("application/pdf")
            },
            flashcards = flashcards,
            showFlashcardDialog = showFlashcardDialog,
            isLoading = isLoading,
            errorMessage = errorMessage,
            onDismissFlashcards = { showFlashcardDialog = false },
            onDismissError = { errorMessage = null },

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