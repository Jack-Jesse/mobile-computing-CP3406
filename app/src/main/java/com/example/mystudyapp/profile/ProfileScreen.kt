package com.example.mystudyapp.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mystudyapp.R
import com.example.mystudyapp.Screen
import com.example.mystudyapp.signin.getUsername
import com.example.mystudyapp.signin.saveUsername
import okhttp3.internal.userAgent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val username = getUsername(context).collectAsState(initial = "Loading...")


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(all = 16.dp), // Changed to 16.dp as a common practice
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display the username
            Text(
                text = "Username: ${username.value}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This is your profile screen.",
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }
}