package com.example.mystudyapp.home

import android.content.Context
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Typography

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
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import com.example.mystudyapp.Screen
import com.example.mystudyapp.data.local.dao.StudyEventDao
import com.example.mystudyapp.ui.theme.provider
import com.example.mystudyapp.data.local.database.EventDatabase
import com.example.mystudyapp.data.local.entity.StudyEvent
import com.example.mystudyapp.signin.getUsername
import com.example.mystudyapp.ui.theme.AppTypography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, context: Context) {
    val usernameFlow = getUsername(context) // Flow<String>
    val username by usernameFlow.collectAsState(initial = "")
    val db = EventDatabase.getDatabase(context)
    val studyDao = db.eventDao()

    // Optional: If you want to display the status in your UI
    var studyTableEmpty by remember { mutableStateOf<Boolean?>(null) } // Null initially, then true/false
    var allEvents by remember { mutableStateOf<List<StudyEvent>>(kotlin.collections.emptyList()) }

    // Perform the database check when the Composable is first launched
    LaunchedEffect(key1 = studyDao) { // Re-run if studyDao instance were to change
        try {
            val count = studyDao.getCount() // Call your suspend fun from the DAO
            studyTableEmpty = count == 0
            if (studyTableEmpty == true) {
                Log.d("DatabaseCheck", "The study_table is empty.")
                println("DatabaseCheck: The study_table is empty.")
            } else {
                allEvents = studyDao.getAllEvents() // Fetch all events if not empty
                Log.d("DatabaseCheck", "The study_table is NOT empty. Count: $count")
                println("DatabaseCheck: The study_table is NOT empty. Count: $count")
            }
        } catch (e: Exception) {
            Log.e("DatabaseCheck", "Error checking if study_table is empty", e)
            println("DatabaseCheck: Error checking if study_table is empty: ${e.message}")
            studyTableEmpty = null // Indicate error or unknown state
        }
    }

    val scope = rememberCoroutineScope()



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
                // FloatingActionButton for adding new events or items
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FloatingActionButton(onClick = { navController.navigate(Screen.MEDIA_UPLOAD) }) {
                    Icon(Icons.Filled.Add, "Add new study material")
                    }
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (studyTableEmpty == true) {
                            Text("No Upcoming Events", style = AppTypography.titleLarge)
                            Text("Your Events Go Here", style = AppTypography.titleMedium)
                            Text("Date: 2025-10-12", style = AppTypography.titleSmall)
                            Text("Time: 10:00 AM", style = AppTypography.displaySmall)
                            Text("Click the (+) button below to start adding!", style = AppTypography.titleMedium)
                        } else if (allEvents.isNotEmpty()) {
                            // Display the first event if the list is not empty
                            Text("Title: ${allEvents[0].title}", style = AppTypography.titleLarge)
                            Text(
                                "Description: ${allEvents[0].description}",
                                style = AppTypography.titleMedium
                            )
                            Text("Date: ${allEvents[0].date}", style = AppTypography.titleSmall)
                            Text("Time: ${allEvents[0].time}", style = AppTypography.displaySmall)
                        }
                    }
                }
            }
        }
    }
}