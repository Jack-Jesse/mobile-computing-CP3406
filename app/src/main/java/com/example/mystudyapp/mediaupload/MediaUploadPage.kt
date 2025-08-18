package com.example.mystudyapp.mediaupload

import FlashcardDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mystudyapp.R
import com.example.mystudyapp.Screen
import com.example.mystudyapp.data.local.database.EventDatabase
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
    errorMessage: String?,
    onDismissFlashcards: () -> Unit,
    onDismissError: () -> Unit,
    eventName: String,
    onEventNameChanged: (String) -> Unit,
    onEventScheduled: (String) -> Unit,
    eventScheduledMessage: String?,
    onDismissEventScheduledMessage: () -> Unit
) {
    val context = LocalContext.current
    var currentFlashcardIndex by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload & Process Media") },
                navigationIcon = {
                     IconButton(onClick = { navController.navigateUp() }) { // <-- Back button
                         Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                     }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
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
                        if (eventName.isNotBlank()) {
                            onEventScheduled("Event '$eventName' scheduled for selected PDF content.")
                        } else {
                            onEventScheduled("PDF content processed (no event name given).")
                        }
                        // Material 3 schedule event logic here
                        // After scheduling (or showing the message), open the DatePicker
                        showDatePicker = true

                        // Clear the event name after scheduling
                        onEventNameChanged("")



                    },
//                    enabled = !isLoading && flashcards.isNotEmpty(), // Enable if not loading AND flashcards are ready

                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Schedule Study Event")
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
                                    // Schedule the actual event using the selected date
                                    scheduleCalendarEvent(context, eventName, millis, flashcards.joinToString("\n\n"))
                                }
                                showDatePicker = false // Close the picker
                            }) { Text("Confirm") }
                        },
                        dismissButton = { Button(onClick = { showDatePicker = false }) { Text("Cancel") } }
                    )
                }


                // --- Display Error Messages ---
//                if (errorMessage != null) {
//                    Text(
//                        text = "Error: $errorMessage",
//                        color = MaterialTheme.colorScheme.error,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                    Button(onClick = onDismissError, modifier = Modifier.padding(top = 4.dp)) {
//                        Text("Dismiss Error")
//                    }
//                }

                // --- Display Event Scheduled Message ---
//                if (eventScheduledMessage != null) {
//                    Text(
//                        text = eventScheduledMessage,
//                        color = MaterialTheme.colorScheme.tertiary, // Or primary
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                    Button(
//                        onClick = onDismissEventScheduledMessage,
//                        modifier = Modifier.padding(top = 4.dp)
//                    ) {
//                        Text("OK")
//                    }
//                }

            } // End of Column for buttons and inputs
        } // End of Box

        // --- Dialog to Show Flashcards ---
        if (showFlashcardDialog && flashcards.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = {
                    onDismissFlashcards() // This is your existing lambda to hide the dialog
                    // Optionally reset index if you want it to always start from the first card
                    // currentFlashcardIndex = 0
                },
                title = {
                    Text("Flashcard ${currentFlashcardIndex + 1} of ${flashcards.size}")
                },
                text = {
                    // Card content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 150.dp), // Give it some minimum height
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
        // You can set an end time as well, e.g., startTimeMillis + one hour
        val endTimeMillis = startTimeMillis + (60 * 60 * 1000) // 1 hour later
        putExtra(android.provider.CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis)
        putExtra(android.provider.CalendarContract.Events.DESCRIPTION, description)
        putExtra(android.provider.CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().timeZone.id)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // Handle the case where no calendar app is available
        // You might show a Toast or log an error
    }
}

//@Composable
//fun FlashcardDisplayDialog(flashcards: List<String>, onDismiss: () -> Unit) {
//    Dialog(onDismissRequest = onDismiss) {
//        Card(modifier = Modifier.padding(16.dp)) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("Generated Flashcards", style = MaterialTheme.typography.headlineSmall)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
//                    items(flashcards) { cardText ->
//                        Text(cardText, modifier = Modifier.padding(bottom = 8.dp))
//                        Divider()
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
//                    Text("Close")
//                }
//            }
//        }
//    }
//}
//
