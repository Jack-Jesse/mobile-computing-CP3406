package com.example.mystudyapp

//import com.example.mystudyapp.ui.theme.MyStudyAppTheme
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.compose.errorContainerDark
import com.example.compose.errorDark
import com.example.compose.errorLight
import com.example.compose.onErrorDark
import com.example.compose.onErrorContainerDark
import com.example.compose.onErrorDarkHighContrast
import com.example.compose.onErrorLight
import com.example.compose.onPrimaryContainerDark
import com.example.compose.onPrimaryContainerLightHighContrast
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.onTertiaryDark
import com.example.compose.onTertiaryLight
import com.example.compose.primaryContainerDarkHighContrast
import com.example.compose.primaryContainerLightHighContrast
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.example.compose.primaryLightHighContrast
import com.example.mystudyapp.data.StudyDatabase
import com.example.mystudyapp.data.StudyEvent
import com.example.mystudyapp.data.StudyEventDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.jvm.java
import java.time.ZoneId.systemDefault


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the Room database instance
        val db = StudyDatabase.getInstance(applicationContext)
        val dao = db.studyEventDao()

        enableEdgeToEdge()
        setContent {
            val darkTheme = remember { mutableStateOf(false) }
            AppTheme(darkTheme = darkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavHost(
                        darkTheme = darkTheme,
                        dao = dao
                    )
//                    StudyScreen(dao = dao)

//                    MaterialTheme {
//                        StudyScreen(dao = dao)
//                    }
                }
//                MaterialTheme {
//                    StudyScreen(dao = dao)
//                }

            }
        }
    }
}

object Screen {
    const val SIGN_IN = "signIn"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val MEDIA_UPLOAD = "mediaUpload"
    const val EDIT_PROFILE = "editProfile"
}

@Composable
fun StudyScreen(dao: StudyEventDao) {
    val scope = rememberCoroutineScope()

    // observe DB
    val events by dao.getAllFlow().collectAsState(initial = emptyList())

    // simple input state
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("09:00") }

    // just use "now" for date demo — swap with a DatePicker later
    val todayMillis = remember { System.currentTimeMillis() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Study name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time (HH:mm)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                if (name.isNotBlank() && time.isNotBlank()) {
                    scope.launch {
                        dao.insert(
                            StudyEvent(
                                name = name.trim(),
                                date = todayMillis,     // replace with picked date
                                time = time.trim()
                            )
                        )
                        name = ""
                    }
                }
            }
        ) {
            Text("Add Study Event")
        }

        Spacer(Modifier.height(16.dp))

        Text("Upcoming", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(events) { e ->
                EventCard(e)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun EventCard(e: StudyEvent) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(e.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Date: ${Date(e.date)} • Time: ${e.time}")
        }
    }
}



@Composable
    fun MyAppNavHost(
        modifier: Modifier = Modifier,
        darkTheme: MutableState<Boolean>, // Receive darkTheme state
        dao: StudyEventDao,
        navController: NavHostController = rememberNavController(),
        startDestination: String = Screen.SIGN_IN
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {

            composable("study") {
                StudyScreen(dao = dao)
            }


            composable(Screen.SIGN_IN) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Pass darkTheme to MySignInSection
                    MySignInSection(modifier = Modifier.fillMaxSize(), navController = navController, darkTheme = darkTheme)
                }
            }
            composable(Screen.HOME) { // Changed "home" to Screen.Home for consistency
                MyHomeScreen(darkTheme = darkTheme,
                    dao = dao,
                    navController = navController,
                    modifier = Modifier.fillMaxSize() // Ensure MyHomeScreen fills the available space
                )
//                StudyScreen(
//                    dao = dao
//                )
            }

            composable(Screen.PROFILE) {
                val usernameState = remember { mutableStateOf("John Doe") }
                Scaffold(
                    topBar = { MyTopAppBar(navController, darkTheme) },
                    bottomBar = { MyBottomAppBar(navController, darkTheme) },

                ) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()) {
                        ProfileScreen(navController = navController, darkTheme = darkTheme, username = usernameState.value) // Pass username
                    }
                }
            }
            composable(Screen.SETTINGS) {
                Scaffold(
                    topBar = { MyTopAppBar(navController = navController, darkTheme = darkTheme) }, // Pass darkTheme
                    bottomBar = { MyBottomAppBar(navController, darkTheme) }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        SettingsScreen(darkTheme = darkTheme)
                    }
                }
            }
            composable(Screen.MEDIA_UPLOAD) {
                Scaffold(
                    topBar = { MyTopAppBar(navController = navController, darkTheme = darkTheme) }, // Pass darkTheme
                    bottomBar = { MyBottomAppBar(navController, darkTheme) }
                ) { innerPadding ->
                    MediaUploadPage(modifier = Modifier.padding(innerPadding))
                }
            }

            // ---- Placeholders for the profile options ----
            composable(Screen.EDIT_PROFILE) {
                val usernameState = remember { mutableStateOf("John Doe") }

                Scaffold(
                    topBar = { MyTopAppBar(navController, darkTheme) },
                    bottomBar = { MyBottomAppBar(navController, darkTheme) }
                ) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()) {
                        EditProfileScreen(
                            navController = navController,
                            currentUsername = usernameState.value,
                            onUsernameChange = { usernameState.value = it }
                        )
                    }

                }
            }
        }
    }

    @Composable
    fun Greeting(modifier: Modifier = Modifier) {
        Text(
            text = "Welcome to Study Tok!",
            modifier = modifier
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyTopAppBar(navController: NavHostController, darkTheme: MutableState<Boolean>? = null) { // Made darkTheme optional
        TopAppBar(
            title = { Text("Study Tok") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate(Screen.HOME) }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = "Arrow Back (Left)",
                        tint = if (darkTheme?.value == true) onPrimaryDark else onPrimaryLight
                        )
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.PROFILE) }) {
                    Icon(
                        Icons.Rounded.Person,
                        contentDescription = "Profile",
                        tint = if (darkTheme?.value == true) onPrimaryDark else onPrimaryLight
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = if (darkTheme?.value == true) onTertiaryDark else onTertiaryLight,
            )
        )
    }

    @Composable
    fun MyScheduleSection(containerColor: Color, textColor: Color) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {
            Text(
                "Schedule",
                color = textColor
            )
        }
        MyModalDatePicker()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyModalDatePicker() {
        var eventTitle by remember { mutableStateOf("") }
        var showDatePicker by remember { mutableStateOf(false) }
        var showTimePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()
        val timePickerState = rememberTimePickerState()
        var selectedDate by remember { mutableStateOf<Long?>(null) }
        var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Event Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Button(onClick = { showDatePicker = true }) {
                Text("Set Date and Time")
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDate = datePickerState.selectedDateMillis
                        showDatePicker = false
                        showTimePicker = true // Show time picker after date is selected
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            TimePickerDialog( // You'll need to implement or import a TimePickerDialog
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedTime = Pair(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }

//        if (selectedDate != null && selectedTime != null && eventTitle.isNotBlank()) {
        if (selectedDate != null && selectedTime != null && eventTitle.isNotBlank()) {
            val newEvent = StudyEvent(
                name = eventTitle,
//                date = SimpleDateFormat("dd/MM/yyyy").format(Date(selectedDate!!)),
                date = selectedDate!!,
                time = String.format("%02d:%02d", selectedTime!!.first, selectedTime!!.second)
            )

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val formattedDate = SimpleDateFormat("dd/MM/yyyy").format(Date(selectedDate!!))
                    val formattedTime = String.format("%02d:%02d", selectedTime!!.first, selectedTime!!.second)
                    Text("EVENT: $eventTitle\n📅DATE: $formattedDate\nTIME: $formattedTime",
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MyUpcomingSection(
        db: StudyDatabase,
        containerColor: Color,
        textColor: Color,
        darkTheme: MutableState<Boolean> // pass this in from your app theme state
    ) {
        // val sessions by db.studyEventDao().getAllFlow().collectAsState(initial = emptyList())

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Upcoming", color = if (darkTheme.value) Color.White else Color.Black)

            // sessions.forEach { event ->
            //     val dateString = java.time.Instant.ofEpochMilli(event.date)
            //         .atZone(java.time.ZoneId.systemDefault())
            //         .format(java.time.format.DateTimeFormatter.ofPattern("EEE d MMM yyyy"))
            //
            //     MyUpcomingCard(
            //         darkTheme = darkTheme,
            //         title = event.name,
            //         date = dateString,
            //         time = event.time
            //     )
            // }
        }
    }

        // Collect flow as state (consider collectAsStateWithLifecycle if you have lifecycle-runtime-compose)
//        val sessions by db.studyEventDao().getAllFlow().collectAsState(initial = emptyList())

//        // 1. Fetching data as a Flow
//        val sessionsFlow = db.studyEventDao().getAllFlow()
//        // 2. Collecting the Flow as State
//        val sessions by sessionsFlow.collectAsState(initial = emptyList())

//        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment =  Alignment.CenterHorizontally
//        ) {
//            Text(
//                "Upcoming",
//                color = textColor
//            )

//            val event = sessions
//                .sortedWith(compareBy<StudyEvent> { it.date }.thenBy { it.time }) // ensure stable ordering
//                .firstOrNull()

//            if (event != null) {
//                val dateString = java.time.Instant.ofEpochMilli(event.date)
//                    .atZone(java.time.ZoneId.systemDefault())
//                    .format(java.time.format.DateTimeFormatter.ofPattern("EEE d MMM yyyy"))

//                MyUpcomingCard(
//                    darkTheme = darkTheme,
//                    title = event.name,
//                    date = dateString as Long?,
//                    time = event.time
//                )
//            } else {
//                MyUpcomingCard(darkTheme = darkTheme)
//            }


            // Display the card from the database if available
//            if (sessions.isNotEmpty()) {
//                val latestSession = sessions.last() // Or however you want to select the session
//                MyUpcomingCard(
//                    darkTheme = remember { mutableStateOf(false) }, // Or pass the actual darkTheme state
////                    date = SimpleDateFormat("dd/MM/yyyy").format(Date(latestSession.date)),
////                    time = latestSession.time
//                )
//            } else {
//                MyUpcomingCard(darkTheme = remember { mutableStateOf(false) }) // Default card
//            }

//        }
//    }


//@Composable
//    fun MyUpcomingCard(darkTheme: MutableState<Boolean>) { // Added darkTheme parameter
//    MyUpcomingCard(darkTheme = darkTheme, date = "12/12/2025", time = "12:00") // Default values
//}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyUpcomingCard(
//    darkTheme: MutableState<Boolean>,
//    title: String = "Study Session",
//    date: String? = null,
//    time: String? = null
    darkTheme: MutableState<Boolean>,
    title: String = "Study Session",
    date: Long? = null,
    time: String? = null
) {

    Text(
        text = "DATE: ${
            date?.let {
                java.time.Instant.ofEpochMilli(it)
                    .atZone(systemDefault())
                    .format(java.time.format.DateTimeFormatter.ofPattern("EEE d MMM yyyy"))
            } ?: "—"
        }\nTIME: ${time ?: "—"}"
    )


    Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
//            border = BorderStroke(2.dp, Color.Gray),
            colors = CardDefaults.cardColors(containerColor = if (darkTheme.value) primaryContainerDarkHighContrast else primaryLightHighContrast)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment =  Alignment.CenterHorizontally,
            ) {
//                Text("Study Session", color = if (darkTheme.value) onPrimaryContainerDark else
//                    onPrimaryContainerLightHighContrast
//                )
                Text(title ?: "Study Session", color = if (darkTheme.value) onPrimaryContainerDark else onPrimaryContainerLightHighContrast) // Use the passed title
                Icon(
                    painter = painterResource(id = R.drawable.calender), // Changed to custom image
                    contentDescription = "Calender",
                    modifier = Modifier
                        .size(80.dp) // Adjust size as needed
                        .clip(RoundedCornerShape(12.dp)), // Add rounded corners
                    tint = Color.Unspecified // To use the original image colors
                )
                if (date != null && time != null) {
                    Text(
                        text = "DATE: $date\nTIME: $time",
                    ) } else {
                        Text("No upcoming event scheduled.")
                    }
//                Text(
//                    text = "DATE: $date\nTIME: $time",
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                    color = if (darkTheme.value) onPrimaryContainerDark else onPrimaryContainerLightHighContrast)
            }
        }
    }

    @Composable
    fun MyBottomAppBar(navController: NavHostController, darkTheme: MutableState<Boolean>) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.List, contentDescription = "Study Material")
            } // Navigate back to home
            Spacer(modifier = Modifier.weight(1f)) // Add spacer to push FAB to the center

            IconButton( // Navigate to MediaUploadPage
                onClick = { navController.navigate(Screen.MEDIA_UPLOAD) },
                modifier = Modifier
                    .weight(1f) // Adjust weight as needed
                    .fillMaxHeight() // Fill the height of the BottomAppBar
                    .background(color = errorDark, CircleShape) // Slightly lighter for highlight
                    .border(BorderStroke(3.dp, onErrorDarkHighContrast), CircleShape) // Darker border for 3D effect
                    .padding(vertical = 4.dp, horizontal = 8.dp) // Adjust padding as needed
            ) {
                Icon(
                    Icons.Rounded.Add,
                    tint = onErrorDark,
                    contentDescription = "Add",
                    modifier = Modifier.fillMaxSize(), // Make the icon fill the IconButton
                )

            }
            Spacer(modifier = Modifier.weight(1f)) // Add spacer to push FAB to the center
            IconButton(onClick = { navController.navigate(Screen.SETTINGS) }, modifier = Modifier.weight(1f)) { // Navigate to settings
                Icon(Icons.Rounded.Settings, contentDescription = "Settings")
            }
        }
    }

    @Composable
    fun MySignInSection(modifier: Modifier, navController: NavHostController, darkTheme: MutableState<Boolean>) {
        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment =  Alignment.CenterHorizontally
                modifier = Modifier
//                .padding()
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Greeting() // Moved to SIGNIN screen
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Increased padding for more space
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            MySignInButton { navController.navigate(Screen.HOME) }

            MyUnderLinedTextButton("Skip Sign In") { navController.navigate(Screen.HOME) }
        }
    }

    @Composable
    fun MySignInButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryContainerLightHighContrast,
                contentColor = onPrimaryContainerLightHighContrast

            )
        ) {
            Text("Sign In")
        }
    }

    @Composable
    fun MyUnderLinedTextButton(text: String, onClick: () -> Unit) {
        TextButton(onClick = onClick) {
            Text(
                text = text,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
//}

@Composable
fun ProfileScreen(navController: NavHostController, darkTheme: MutableState<Boolean>, username: String) {
    val usernameState = remember { mutableStateOf("John Doe") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = "Profile Icon",
            modifier = Modifier.size(100.dp) // Larger icon for profile
        )
        Text(
            text = username, // Use the username state
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        Text(
            text = "john.doe@example.com", // Replace with dynamic user email
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Profile options
        ProfileOptionItem("Edit Profile") { navController.navigate(Screen.EDIT_PROFILE) }
        ProfileOptionItem("Settings") { navController.navigate(Screen.SETTINGS) }

        // Spacer to push the logout button to the bottom
        Spacer(modifier = Modifier.weight(1f))


        Spacer(modifier = Modifier.weight(1f)) // Pushes logout button to the bottom

        Button( // TODO: Implement actual logout logic
            onClick = { /* Handle logout */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = errorLight, // Using red from your color.kt
                contentColor = onErrorLight
            )
        ) {
            Text("Logout")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController, currentUsername: String, onUsernameChange: (String) -> Unit) {
    var newUsername by remember { mutableStateOf(currentUsername) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = newUsername,
                onValueChange = { newUsername = it },
                label = { Text("Username") }
            )
            Button(onClick = { onUsernameChange(newUsername); navController.popBackStack() }) { Text("Save") }
        }
    }
}
// Placeholder for TimePickerDialog - you might need to create a more robust one
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Time") },
        text = content,
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberTimePickerState(initialHour: Int = 0, initialMinute: Int = 0, is24Hour: Boolean = true): androidx.compose.material3.TimePickerState {
    return androidx.compose.material3.rememberTimePickerState(initialHour, initialMinute, is24Hour)
}

@Composable
fun ProfileOptionItem(optionText: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = optionText, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun SettingsScreen(darkTheme: MutableState<Boolean>) {
    var isDarkThemeEnabled by remember { darkTheme }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "Settings Icon",
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "Settings",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode")
            Switch(
                checked = isDarkThemeEnabled,
                onCheckedChange = { isDarkThemeEnabled = it }
            )
        }
        Spacer(modifier = Modifier.weight(1f)) // Pushes logout button to the bottom
        Button(
            onClick = { /* Handle logout */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkThemeEnabled) errorContainerDark else errorLight,
                contentColor = if (isDarkThemeEnabled) onErrorContainerDark else onErrorLight

            )
        ) {
            Text("Logout")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaUploadPage(modifier: Modifier = Modifier) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(topBar = { /* use parent app bar or add your own */ }) { innerPadding -> // This Scaffold can be removed if handled by NavHost
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                text = "Upload Your Study Materials",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // PDF Upload Button
            Button(
                onClick = { /* Handle PDF upload */ },
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
                        contentDescription = "Upload PDF",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(vertical = 8.dp) // Add some padding around the icon
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp)) // Shadow for the icon
                            .size(100.dp)
                    )
                    Text("Upload PDF")
                }
            }

            // Audio Upload Button
            Button(
                onClick = { /* Handle PDF upload */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(25.dp) // Rounded corners for the button itself
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.mp3),
                        contentDescription = "Upload PDF",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(vertical = 8.dp) // Add some padding around the icon
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp)) // Shadow for the icon
                            .size(100.dp)
                    )
                    Text("Upload Audio")
                }
            }

            // Video Upload Button
            Button(
                onClick = { /* Handle PDF upload */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(25.dp) // Rounded corners for the button itself
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.mp4),
                        contentDescription = "Upload PDF",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(vertical = 8.dp) // Add some padding around the icon
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(15.dp)) // Shadow for the icon
                            .size(100.dp)
                    )
                    Text("Upload Audio")
                }
            }
        }
            }
        }
    }

@Composable
fun MyHomeScreen(navController: NavHostController, modifier: Modifier = Modifier, darkTheme: MutableState<Boolean>, dao: StudyEventDao) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MyTopAppBar(navController = navController, darkTheme = darkTheme) }, // Pass darkTheme
        bottomBar = { MyBottomAppBar(navController, darkTheme) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            StudyScreen(dao = dao)

//            if (darkTheme.value) {
//                MyScheduleSection(
//                    containerColor = primaryDark,
//                    textColor = onPrimaryContainerDark // When dark mode
//                )
//                MyUpcomingSection( // Pass darkTheme to MyUpcomingSection
////                    db = db,
//                    containerColor = primaryDark,
//                    textColor = onPrimaryContainerDark
//                )
//            } else {
//                MyScheduleSection(
//                    containerColor = primaryLight,
//                    textColor = primaryLight // When light mode text is dark
//                )
//                MyUpcomingSection( // Pass darkTheme to MyUpcomingSection
//                    db = db,
//                    containerColor = primaryLight,
//                    textColor = primaryLight // When light mode text is dark
//                )
//            }
        }
    }
}