package com.example.mystudyapp

//import com.example.mystudyapp.ui.theme.MyStudyAppTheme
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
//import com.example.mystudyapp.activities.HomeScreen
import com.example.compose.AppTheme
import com.example.mystudyapp.api.apiKey
import com.example.mystudyapp.api.endpoint
import com.example.mystudyapp.api.model.makeGeminiRequestWithCoroutine
import com.example.mystudyapp.api.request


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = remember { mutableStateOf(false) }
            // Testing
            val navController = rememberNavController()
            AppTheme(darkTheme = darkTheme.value) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    AppNavHost(navController = NavHostController(this))   // Can maybe delete this
                    AppNavHost(navController = navController)
                    makeGeminiRequestWithCoroutine(apiKey, endpoint, request)

                }
            }
        }
    }
}
