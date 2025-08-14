package com.example.mystudyapp.mediaupload

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.example.mystudyapp.Screen
import com.example.mystudyapp.data.local.entity.StudyEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.text.format
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.Long



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreation(
    onDismiss: () -> Unit,
    onEventCreated: (StudyEvent) -> Unit,
    navController: NavController
) {
    var currentStep by remember { mutableStateOf(EventCreationStep.Date) }

    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf<Int?>(null) }
    var selectedMinute by remember { mutableStateOf<Int?>(null) }
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState() // Assuming you have a TimePickerState

    when (currentStep) {
        EventCreationStep.Date -> {
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = {
                        selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            currentStep = EventCreationStep.Time
                        } else {
                            // Handle case where no date is selected, or show error
                            onDismiss()
                        }
                    }) { Text("Next") }
                },
                dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        EventCreationStep.Time -> {
            TimePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        currentStep = EventCreationStep.Details
                    }) { Text("Next") }
                },
                dismissButton = {
                    TextButton(onClick = { currentStep = EventCreationStep.Date }) { Text("Back") }
                }
            ) {
                TimePicker(state = timePickerState) // Material 3 TimePicker
            }
        }

        EventCreationStep.Details -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("Event Details") },
                text = {
                    Column {
                        TextField(
                            value = eventName,
                            onValueChange = { eventName = it },
                            label = { Text("Event Name") },
                            singleLine = true
                        )
                        Spacer(Modifier.height(8.dp))
                        TextField(
                            value = eventDescription,
                            onValueChange = { eventDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier.height(120.dp) // Example height for multi-line
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        // Ensure all required data is present
                        if (selectedDateMillis != null && selectedHour != null && selectedMinute != null && eventName.isNotBlank()) {
                            // Combine date and time into a single timestamp
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = selectedDateMillis!!
                                set(Calendar.HOUR_OF_DAY, selectedHour!!)
                                set(Calendar.MINUTE, selectedMinute!!)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            val eventTimestamp = calendar.timeInMillis

                            val newEvent = StudyEvent(
                                // StudyEvent has an auto-generated ID,
                                // id = auto

                                title = eventName,
                                description = eventDescription,
                                date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time),
                                time = SimpleDateFormat("hh:mm a").format(calendar.time)
                            )
                            onEventCreated(newEvent) // Pass the created event
                            navController.navigate(Screen.HOME)
                            onDismiss() // Dismiss the dialog after creation
                        } else {
                            // Optionally, show an error message if data is missing
                        }
                    }) { Text("Create Event") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        currentStep = EventCreationStep.Time
                    }) { Text("Back") }
                }
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit, // This will be the TimePicker
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = content,
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}