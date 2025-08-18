package com.example.mystudyapp // ENSURE THIS PACKAGE IS THE SAME AS AppNavHost.kt

// ... other necessary imports ...
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable //Needed for the preview
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
import com.example.mystudyapp.signin.SignInScreen // Make sure this import is correct

// ... imports for your other screens like Home, MediaUpload etc. ...

// THE ONLY Screen OBJECT
object Screen {
    const val SIGN_IN = "signIn" // <<< Double check this exact string value
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val MEDIA_UPLOAD = "mediaUpload"
    const val EDIT_PROFILE = "editProfile"
    // ... any other routes
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.appNavGraph(navController: NavHostController) {
    // THIS COMPOSABLE IS ESSENTIAL for the startDestination
    composable(Screen.SIGN_IN) { // <<< Uses the Screen.SIGN_IN ("signIn") from above
        SignInScreen(navController = navController) // Your actual SignInScreen composable
    }

    // Other screens
    composable(Screen.HOME) {
        // Your HomeScreen(navController)
        HomeScreen(
            navController = navController,
            // Pass any other parameters your HomeScreen needs, e.g., context
            context = LocalContext.current
        )
    }
    composable(Screen.MEDIA_UPLOAD) {
        // Your MediaUploadScreen(navController)
        // === This is where we set up everything MediaUploadPage needs ===
        val context = LocalContext.current

        // States that your MediaUploadPage likely needs (based on previous versions)
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

            // --- ENSURE THESE PARAMETERS ARE PRESENT AND CORRECT ---
            eventName = eventName,
            onEventNameChanged = { newName -> // <<< THIS IS THE MISSING/INCORRECT PARAMETER
                eventName = newName
            },
            onEventScheduled = { message ->
                eventScheduledMessage = message
                // Optionally clear eventName or do other UI updates
                // eventName = ""
            },
            eventScheduledMessage = eventScheduledMessage,
            onDismissEventScheduledMessage = { eventScheduledMessage = null }
            // Add any other parameters your MediaUploadPage composable function requires
        )
    }
}