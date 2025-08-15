package com.example.mystudyapp



import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mystudyapp.home.HomeScreen
import com.example.mystudyapp.mediaupload.MediaUploadPage // Assuming MediaUploadPage is in this package
import com.example.mystudyapp.signin.SignInScreen


//fun NavGraphBuilder.AppNavGraph(navController: NavHostController) {
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.appNavGraph(navController: NavHostController) {

    composable(Screen.SIGN_IN) {
        SignInScreen(navController)
    }
    composable(Screen.HOME) {
        HomeScreen(navController, context = LocalContext.current)
    }
    composable(Screen.MEDIA_UPLOAD) {
        // Testing
        // Create pickers here and pass their launchers to the UI
        val onPickCreateEvent = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            // TODO: hand off to VM if needed (e.g., hiltViewModel().onPdfPicked(uri))
        }

        val onPickUploadMedia = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri -> /* TODO: send to VM */ }

        MediaUploadPage(
            onPickUploadMedia = { onPickUploadMedia.launch("application/pdf") },
            onEventScheduled = {onEventScheduled -> /* TODO: send to VM */ },
            onEventNameChanged = { onEventNameChanged -> /* TODO: send to VM */ },
            navController = navController,
        )




    }
}
