package com.example.mystudyapp.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp
import androidx.room.RoomDatabase
import com.example.mystudyapp.Screen
//import com.example.mystudyapp.data.local.database.EventDatabase
import com.example.mystudyapp.data.local.entity.StudyEvent
import com.example.mystudyapp.signin.getUsername
import com.example.mystudyapp.AppTypography
import com.example.mystudyapp.data.local.dao.StudyEventDao
import com.example.mystudyapp.data.local.database.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, context: Context) {
    val usernameFlow = getUsername(context) // Flow<String>
    val username by usernameFlow.collectAsState(initial = "")

    val eventDao = remember { AppDatabase.getDatabase(context).studyEventDao() }

    val latestEventState by eventDao.getLatestEvent().collectAsState(initial = null)

    var studyTableEmpty by remember { mutableStateOf<Boolean?>(null) } // Null initially, then true/false
    var allEvents by remember { mutableStateOf<List<StudyEvent>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {}
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Is study_table empty? ${if (studyTableEmpty == true) "Yes" else "Not empty"}"
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (username.isNotEmpty()) {
                        "Welcome, $username!"
                    } else {
                        "Welcome!"
                    }
                )

                // Upcoming Event Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {

                        Text(
                            "Latest Event", // Changed title
                            style = AppTypography.titleLarge,
                        )

                        // Get the event from the state
//                        val eventToShow = latestEventState
                        val eventToDisplay = latestEventState // Use the state directly

                        // Log the state REGARDLESS of whether it's null or not, to see what it is
                        Log.d("HOME_SCREEN_UI", "Current latestEventState from DAO: $eventToDisplay")

                        if (eventToDisplay == null) {
                            // This block means no latest event was found in the database by the query
                            Log.d("HOME_SCREEN_UI", "No latest event found (eventToDisplay is null). Displaying placeholders.")
                            Text("No Upcoming Events", style = AppTypography.titleLarge)
                            Text("Your Events Go Here", style = AppTypography.titleMedium)
                            Text("Date: 2025-10-12", style = AppTypography.titleSmall) // Example placeholder
                            Text("Time: 10:00 AM", style = AppTypography.displaySmall) // Example placeholder
                            Text(
                                "Click the (+) button below to start adding!",
                                style = AppTypography.titleMedium
                            )
                        } else {
                            // This block means an event object was successfully retrieved
                            Log.d("HOME_SCREEN_UI", "Displaying event: Title='${eventToDisplay.title}', Date='${eventToDisplay.date}'")
                            Text(
                                "Title: ${eventToDisplay.title}",
                                style = AppTypography.titleMedium
                            )
                            Text(
                                "Description: ${eventToDisplay.description}",
                                style = AppTypography.bodyMedium
                            )
                            eventToDisplay.date?.let {
                                Text("Date: $it", style = AppTypography.bodySmall)
                            }
                            eventToDisplay.time?.let {
                                Text("Time: $it", style = AppTypography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
