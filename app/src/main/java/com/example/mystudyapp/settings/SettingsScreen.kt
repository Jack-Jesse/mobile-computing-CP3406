package com.example.mystudyapp.settings


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Switch
import androidx.navigation.NavController
import com.example.mystudyapp.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkTheme: Boolean,       // Receive the current theme state
    onThemeToggle: () -> Unit   // Receive the function to call when switch is toggled
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Home icon button
                    IconButton(onClick = { navController.navigate(Screen.HOME) }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Settings Icon on the left
                IconButton(onClick = { navController.navigate(Screen.SETTINGS) }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
                // FloatingActionButton for adding new events or items in the center
                Box(
                    modifier = Modifier.weight(1f), // Occupy remaining space to center FAB
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier, // Let the content define the size
                        contentAlignment = Alignment.Center // Center the FAB within this Box
                    ) {
                        FloatingActionButton(
                            onClick = { navController.navigate(Screen.MEDIA_UPLOAD) }
                        ) {
                            Icon(Icons.Filled.Add, "Add new study material")
                        }
                    }
                }
                // Profile Icon on the right
                IconButton(onClick = { navController.navigate(Screen.PROFILE) }) {
                    Icon(Icons.Filled.Person, contentDescription = "Profile")
                }
            }
        }    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings Screen Content Goes Here")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Mode")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeToggle() }
                )
            }
        }
    }
}
