package com.example.mystudyapp.mediaupload

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mystudyapp.R
import com.example.mystudyapp.Screen
import com.example.mystudyapp.data.local.database.EventDatabase
import com.example.mystudyapp.mediaupload.UploadPdfScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaUploadPage(
    onPickUploadMedia: () -> Unit,
    onEventScheduled: (selectedDateMillis: Long?) -> Unit, // Callback with the selected date
    onEventNameChanged: (String) -> Unit, // Callback for event name changes
    navController: NavController,
//    onPickCreateEvent: () -> Unit,
) {


    // Testing
    var showEventNameDialog by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var eventName by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
    var selectedDateText by remember { mutableStateOf("Schedule Event") } // To update button text
//    var formattedEventName by remember { mutableStateOf("") }

    val context = LocalContext.current.applicationContext
    val composableScope = rememberCoroutineScope() // Or a more global scope if appropriate
    val studyEventDao = remember {
        EventDatabase.getDatabase(context, composableScope /* or Dispatchers.IO if getDatabase manages its own scope internally for callbacks */).eventDao()
    }


    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text("Study Tools") },
            )
         }) { innerPadding -> // This Scaffold can be removed if handled by NavHost
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Media Upload Button
                Button(
                    onClick = onPickUploadMedia,
                            colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 1.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(25.dp) // Rounded corners for the button itself
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.pdf),
                            contentDescription = "Upload Media",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(vertical = 8.dp) // Add some padding around the icon
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp)) // Shadow for the icon
                                .size(100.dp)
                        )
                        Text("Upload Media")
                    }
                }

                // Schedule Event Button
                Button(
                    onClick = { showDatePickerDialog = true; showEventNameDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 1.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(25.dp) // Rounded corners for the button itself
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.calender),
                            contentDescription = "Schedule Event",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(vertical = 8.dp) // Add padding around the icon
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp)) // Shadow for the icon
                                .size(100.dp)
                        )
                        Text("Schedule Event")
                    }
                }


                if (showDatePickerDialog) {
                    EventCreation(
                        onDismiss = { showDatePickerDialog = false },
                        onEventCreated = {
                            navController.navigate(Screen.HOME)
                        },
                        navController = navController,
                        studyEventDao = studyEventDao,
                        composableScope
                    )
                }

            }
        }
    }
}
