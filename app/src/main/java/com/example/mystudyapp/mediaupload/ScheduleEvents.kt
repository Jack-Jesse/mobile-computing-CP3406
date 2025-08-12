package com.example.mystudyapp.mediaupload

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mystudyapp.data.local.entity.StudyEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleQuickAddSheet(
    onDismiss: () -> Unit,
    onSaved: (StudyEvent) -> Unit,
) {
    val title = remember { mutableStateOf("") }


    val description = remember { mutableStateOf("") }

    // Date picker
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = remember { mutableStateOf("") }
    val selectedDateMillis = datePickerState.selectedDateMillis


    // Time picker
    val time = remember { mutableStateOf("") }



}