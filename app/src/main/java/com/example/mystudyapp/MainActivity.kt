package com.example.mystudyapp

// ... imports ...
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme // If you use it
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.mystudyapp.ui.theme.AppTheme // Your theme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // If you have this
        setContent {
            val darkTheme = remember { mutableStateOf(false) } // From your original code
            AppTheme(darkTheme = darkTheme.value) { // Your theme
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // This is where AppNavHost is called (around line 38)
                    AppNavHost() // This uses the default rememberNavController()
                }
            }
        }
    }
}