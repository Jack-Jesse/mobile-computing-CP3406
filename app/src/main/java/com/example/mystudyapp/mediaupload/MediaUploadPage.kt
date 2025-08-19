package com.example.mystudyapp.mediaupload

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mystudyapp.R
import com.example.mystudyapp.Screen
import com.example.mystudyapp.data.local.database.AppDatabase
import com.example.mystudyapp.data.local.entity.StudyEvent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaUploadPage(
    navController: NavHostController,
    onPickUploadMedia: () -> Unit,
    flashcards: List<String>,
    showFlashcardDialog: Boolean,
    isLoading: Boolean,
//    errorMessage: String?,
    onDismissFlashcards: () -> Unit,
//    onDismissError: () -> Unit,
    eventName: String,
    onEventNameChanged: (String) -> Unit,
    onEventScheduled: (String) -> Unit,
    eventScheduledMessage: String?,
    onDismissEventScheduledMessage: () -> Unit
) {
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).studyEventDao() // <<< MUST MATCH HomeScreen's DB
    val coroutineScope = rememberCoroutineScope() // To launch suspend functions

    var currentFlashcardIndex by remember { mutableIntStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showEventNameDialog by remember { mutableStateOf(false) }
    var tempEventName by remember { mutableStateOf("") }
    var tempEventDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Media Upload") },
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
    ) { innerPadding ->
        Box( // Use Box to center the Column
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center // Centers the Column in the Box
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize(), // Make the Column wrap its content
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between items in the column
            ) {

                // --- Loading Indicator ---
                if (isLoading) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Processing PDF, please wait...")
                }

                // --- Button to Pick PDF ---
                Button(
                    onClick = onPickUploadMedia,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .size(140.dp) // Total button size (image area)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pdf),
                        contentDescription = "Select PDF",
                        modifier = Modifier.fillMaxSize(), // Fill inside the button
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = "Upload PDF to Create Flashcards",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 0.dp)
                )

            // Button to Schedule Event
                Button(
                    onClick = {
                        showEventNameDialog = true
                    },

                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Schedule Study Event")
                }

                // Dialog to Enter Event Name and Description
                if (showEventNameDialog) {
                    AlertDialog(
                        onDismissRequest = { showEventNameDialog = false },
                        title = { Text("Event Details") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = tempEventName,
                                    onValueChange = { tempEventName = it },
                                    label = { Text("Event Name (Optional)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = tempEventDescription,
                                    onValueChange = { tempEventDescription = it },
                                    label = { Text("Event Description (Optional)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                onEventNameChanged(tempEventName) // Update eventName

                                // Proceed to date picker
                                showEventNameDialog = false
                                showDatePicker = true

                                // Prepare for scheduling
                                val eventTitle = tempEventName.ifBlank { "Study Session" }
//                                val effectiveDescription = tempEventDescription.ifBlank { "Review flashcards generated from the PDF." }

                                if (eventTitle.isNotBlank()) {
                                    onEventScheduled("Event '$eventTitle' details captured. Please select date and time.")
                                } else {
                                    onEventScheduled("Details captured. Please select date and time for your study session.")
                                }
                                // The actual calendar event scheduling will happen after time is picked.
                            }) { Text("Next") }
                        },
                        dismissButton = { Button(onClick = { showEventNameDialog = false }) { Text("Cancel") } }
                    )
                }

                // --- Date Picker Dialog ---
                if (showDatePicker) {
                    val datePickerState = rememberDatePickerState()

                    AlertDialog(
                        onDismissRequest = { showDatePicker = false },
                        title = { Text("Select Event Date") },
                        text = { DatePicker(state = datePickerState) },
                        confirmButton = {
                            Button(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    showDatePicker = false // Close date picker
                                    showTimePicker = true // Open time picker
                                } ?: run {
                                    onEventScheduled("Please select a date.")
                                }
                            }) { Text("Confirm") }
                        },
                        dismissButton = { Button(onClick = { showDatePicker = false }) { Text("Cancel") } }
                    )
                }

                // --- Time Picker Dialog ---
                if (showTimePicker) {
                    val timePickerState = rememberTimePickerState()
                    AlertDialog(
                        onDismissRequest = { showTimePicker = false },
                        title = { Text("Select Event Time") },
                        text = {
                            androidx.compose.material3.TimePicker(state = timePickerState)
                               },
                        confirmButton = {
                            Button(onClick = {
                                showTimePicker = false
                                val calendar = Calendar.getInstance().apply {
                                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    set(Calendar.MINUTE, timePickerState.minute)
                                }

                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // Or "hh:mm a" for AM/PM

                                val eventToSave = StudyEvent(
                                    title = tempEventName,
                                    description = tempEventDescription,
                                    date = dateFormat.format(calendar.time),
                                    time = timeFormat.format(calendar.time)
                                )


                                coroutineScope.launch {
                                    try {
                                        dao.insertStudyEvent(eventToSave)
                                        Log.d("DB_INSERT", "Event insertion successful: ${eventToSave.title}")
                                        onEventScheduled("Event '${eventToSave.title}' scheduled successfully!")
                                        showTimePicker = false
                                    } catch (e: Exception) {
                                        onEventScheduled("Failed to schedule event. Error: ${e.message}")
                                    }
                                }




                                val finalEventTimeMillis = calendar.timeInMillis
                                val eventTitle = eventName.ifBlank { "Flashcards Study Session" }
                                val eventDesc = tempEventDescription.ifBlank { "Create more flashcards from a PDF." }
                                scheduleCalendarEvent(context, eventTitle, finalEventTimeMillis, eventDesc)
                                onEventScheduled("Event '$eventTitle' scheduled!")                             }) { Text("Confirm") }
                        },
                        dismissButton = { Button(onClick = { showDatePicker = false }) { Text("Cancel") } }
                    )
                }


                // --- Display Event Scheduled Message ---
                if (eventScheduledMessage != null) {
                    Text(
                        text = eventScheduledMessage,
                        color = MaterialTheme.colorScheme.tertiary, // Or primary
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Button(
                        onClick = onDismissEventScheduledMessage,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("OK")
                    }
                }
            }
        }

        // --- Dialog to Show Flashcards ---
        if (showFlashcardDialog && flashcards.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = {
                    onDismissFlashcards()
                },
                title = {
                    Text("Flashcard ${currentFlashcardIndex + 1} of ${flashcards.size}")
                },
                text = {
                    // Card content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 150.dp), // Give minimum height
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = flashcards[currentFlashcardIndex],
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (currentFlashcardIndex > 0) {
                                    currentFlashcardIndex--
                                }
                            },
                            enabled = currentFlashcardIndex > 0 // Disable if it's the first card
                        ) {
                            Text("Back")
                        }

                        Button(
                            onClick = onDismissFlashcards // existing dismiss lambda
                        ) {
                            Text("Dismiss")
                        }

                        Button(
                            onClick = {
                                if (currentFlashcardIndex < flashcards.size - 1) {
                                    currentFlashcardIndex++
                                }
                            },
                            enabled = currentFlashcardIndex < flashcards.size - 1 // Disable if it's the last card
                        ) {
                            Text("Next")
                        }
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun scheduleCalendarEvent(context: Context, title: String, startTimeMillis: Long, description: String) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = android.provider.CalendarContract.Events.CONTENT_URI
        putExtra(android.provider.CalendarContract.Events.TITLE, title.ifBlank { "Study Session" })
        putExtra(android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMillis)
        val endTimeMillis = startTimeMillis + (60 * 60 * 1000) // 1 hour later
        putExtra(android.provider.CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis)
        putExtra(android.provider.CalendarContract.Events.DESCRIPTION, description)
        putExtra(android.provider.CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().timeZone.id)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}