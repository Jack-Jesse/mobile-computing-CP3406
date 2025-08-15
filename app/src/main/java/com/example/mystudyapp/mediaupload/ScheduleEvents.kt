package com.example.mystudyapp.mediaupload

import android.os.Build
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystudyapp.data.local.dao.StudyEventDao
import com.example.mystudyapp.data.local.entity.StudyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.Long

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreation(
    onDismiss: () -> Unit,
    onEventCreated: (StudyEvent) -> Unit,
    navController: NavController,
    studyEventDao: StudyEventDao, // Inject Dao
    coroutineScope: CoroutineScope // Inject CoroutineScope
) {
    var currentStep by remember { mutableStateOf(EventCreationStep.Date) }

    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf<Int?>(null) }
    var selectedMinute by remember { mutableStateOf<Int?>(null) }
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    var formattedTime by remember { mutableStateOf<String?>(null) }
    var formattedDate by remember { mutableStateOf<String?>(null) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState() // Assuming you have a TimePickerState

    // Get a CoroutineScope that is tied to this Composable's lifecycle
    val composableScope = rememberCoroutineScope()

    when (currentStep) {
        EventCreationStep.Date -> {
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = {
                        selectedDateMillis = datePickerState.selectedDateMillis
                        selectedDateMillis?.let {
                            val calendar = Calendar.getInstance().apply { timeInMillis = it }
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            formattedDate = dateFormat.format(calendar.time)
                            println(formattedDate) // Print formatted date
                        }
                        if (selectedDateMillis != null) {
                            currentStep = EventCreationStep.Time
                        } else {
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

                        formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                        println(formattedTime) // Print formatted time
                    }) { Text("Next") }
                },
                dismissButton = {
                    TextButton(onClick = { currentStep = EventCreationStep.Date }) { Text("Back") }
                }
            ) {
                TimePicker(state = timePickerState)
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
                            modifier = Modifier.height(120.dp)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (selectedDateMillis != null && selectedHour != null && selectedMinute != null && eventName.isNotBlank()) {
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = selectedDateMillis!!
                            }

                            // Testing Event Creation
                            val newEvent = StudyEvent(
                                title = eventName,
                                description = eventDescription,
                                date = formattedDate,
                                time = formattedTime
                            )

                            // Insert into Database
                            composableScope.launch {
                                studyEventDao.insert(newEvent)
                            }

                            onEventCreated(newEvent)
                            // Testing Event Creation print to log
                            println(eventName)
                            println(eventDescription)
                            println(formattedDate)
                            println(formattedTime)
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