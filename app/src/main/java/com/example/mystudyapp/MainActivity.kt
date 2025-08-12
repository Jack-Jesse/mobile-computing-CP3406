package com.example.mystudyapp

//import com.example.mystudyapp.ui.theme.MyStudyAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.mystudyapp.activities.HomeScreen
import com.example.mystudyapp.home.HomeScreen
import com.example.compose.AppTheme
import com.example.mystudyapp.signin.SignInScreen


class MainActivity : ComponentActivity() {
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
                }
            }
        }
    }
}
