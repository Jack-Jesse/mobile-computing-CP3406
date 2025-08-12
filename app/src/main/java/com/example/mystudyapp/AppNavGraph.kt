package com.example.mystudyapp



import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystudyapp.home.HomeScreen
import com.example.mystudyapp.mediaupload.MediaUploadPage // Assuming MediaUploadPage is in this package
import com.example.mystudyapp.signin.SignInScreen
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.mystudyapp.mediaupload.MediaUploadPage



//fun NavGraphBuilder.AppNavGraph(navController: NavHostController) {
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
        val pickPdf = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            // TODO: hand off to VM if needed (e.g., hiltViewModel().onPdfPicked(uri))
        }

        val pickAudio = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri -> /* TODO: send to VM */ }

        val pickVideo = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri -> /* TODO: send to VM */ }



        MediaUploadPage(
            onPickPdf = { /* launch picker */ },
            onPickAudio = { /* launch picker */ },
            onPickVideo = { /* launch picker */ }
        )
    }
}
