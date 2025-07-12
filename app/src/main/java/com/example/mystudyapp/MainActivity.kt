package com.example.mystudyapp

import android.os.Bundle
import androidx.compose.material3.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystudyapp.ui.theme.MyStudyAppTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color



class MainActivity : ComponentActivity() {
    private val darkTheme = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStudyAppTheme(darkTheme = darkTheme.value) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )
                        MySignInSection(
                            modifier = Modifier.padding(horizontal = 0.dp)
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
    fun MyTopAppBar() {
        TopAppBar(
            title = { Text("Study Tok") },
            navigationIcon = {
                IconButton(onClick = { /* ToDo */ }) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Menu"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle profile click */ }) {
                    Icon(Icons.Rounded.Person, contentDescription = "Profile")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(87, 143, 202)
            )
        )
    }

    @Composable
    fun MyScheduleSection() {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {
            Text("Schedule")
        }
        MyModalDatePicker()
    }

    @Composable
    fun MyModalDatePicker() {
        Card(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            border = BorderStroke(2.dp, Color.Gray),
            colors = CardDefaults.cardColors(Color(54, 116, 181))
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment =  Alignment.CenterHorizontally,
            ) {
                Text("Modal Date Picker")
                Icon(
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = "Calender"
                )
                Text("📅DATE: 12/12/2025\n🕐TIME: 12:00")
            }
        }
    }

    @Composable
    fun MyUpcomingSection() {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {
            Text("Upcoming")
            MyUpcomingCard()
        }
    }

    @Composable
    fun MyUpcomingCard() {
        Card(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            border = BorderStroke(2.dp, Color.Gray),
            colors = CardDefaults.cardColors(Color(245, 240, 205))
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment =  Alignment.CenterHorizontally,
            ) {
                Text("Study Session")
                Icon(
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = "Calender"
                )
                Text("📅DATE: 12/12/2025\n🕐TIME: 12:00")
            }
        }
    }

    @Composable
    fun MyBottomAppBar() {
        BottomAppBar {
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.Home, contentDescription = "Home")
            }
            IconButton(
                onClick = { /* doSomething() */ },
                modifier = Modifier
                    .border(BorderStroke(2.dp, Color.Transparent), CircleShape)
                    .padding(0.dp)
                    .background(Color(110, 194, 7), CircleShape) // Pink background
                    .aspectRatio(1f)
            ) {
                Icon(Icons.Rounded.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(55.dp)
                )
            }
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.DateRange, contentDescription = "Calendar")
            }
        }
    }

    @Composable
    fun MySecondaryBottomAppBar() {
        BottomAppBar {
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
            }
            IconButton(
                onClick = { /* doSomething() */ },
                modifier = Modifier
                    .border(BorderStroke(2.dp, Color.Transparent), CircleShape)
                    .padding(0.dp)
//                    .background(Color(110, 194, 7), CircleShape)
                    .aspectRatio(1f)
            ) {
                Icon(Icons.Rounded.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(55.dp)
                )
            }
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.DateRange, contentDescription = "Calendar")
            }
        }
    }


    @Composable
    fun MySignInSection(modifier: Modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Password") },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            MySignInButton()

            MyUnderLinedTextButton("Skip Sign In")
        }
    }

    @Composable
    fun MySignInButton() {
        Button(
            onClick = { /* ToDo */ }
        ) {
            Text("Sign In")
        }
    }

    @Composable
    fun MyUnderLinedTextButton(text: String) {
        TextButton(onClick = { /* ToDo */ }) {
            Text(
                text = text,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}

@Composable
fun ProfileScreen() {
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
            text = "John Doe", // Replace with dynamic user name
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        Text(
            text = "john.doe@example.com", // Replace with dynamic user email
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Profile options
        ProfileOptionItem("Edit Profile") { /* Handle Edit Profile */ }
        ProfileOptionItem("Change Password") { /* Handle Change Password */ }
        ProfileOptionItem("Notification Settings") { /* Handle Notification Settings */ }

        Spacer(modifier = Modifier.weight(1f)) // Pushes logout button to the bottom

        Button(onClick = { /* Handle logout */ }) {
            Text("Logout")
        }
    }
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
        Button(onClick = { /* Handle logout */ }) {
            Text("Logout")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaUploadPage() {
    Scaffold(
        topBar = { MainActivity().MyTopAppBar() }
    ) { innerPadding ->
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
//            Icon(Icons.Filled.PictureAsPdf, contentDescription = "Upload PDF", modifier = Modifier.padding(end = 8.dp))
                Text("Upload PDF")
            }

            // Audio Upload Button
            Button(
                onClick = { /* Handle Audio upload */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
//            Icon(Icons.Filled.Audiotrack, contentDescription = "Upload Audio", modifier = Modifier.padding(end = 8.dp))
                Text("Upload Audio")
            }

            // Video Upload Button
            Button(
                onClick = { /* Handle Video upload */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
//            Icon(Icons.Filled.Videocam, contentDescription = "Upload Video", modifier = Modifier.padding(end = 8.dp))
                Text("Upload Video")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MediaUploadPagePreview() {
    MyStudyAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { MainActivity().MySecondaryBottomAppBar() } // Use the secondary bottom app bar
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                MediaUploadPage()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyHomeScreenPreview() {
    MyStudyAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { MainActivity().MyBottomAppBar() }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainActivity().MyTopAppBar()
                MainActivity().MyScheduleSection()
                MainActivity().MyUpcomingSection()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MySignInSectionPreview() {
    MyStudyAppTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainActivity().Greeting(
                    modifier = Modifier.padding(it)
                )
                MainActivity().MySignInSection(
                    modifier = Modifier.padding(horizontal = 0.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val darkTheme = remember { mutableStateOf(false) }
    MyStudyAppTheme(darkTheme = darkTheme.value) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MainActivity().MyTopAppBar() },
            bottomBar = { MainActivity().MySecondaryBottomAppBar(
            ) }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                SettingsScreen(darkTheme = darkTheme)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MyStudyAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MainActivity().MyTopAppBar() },
            bottomBar = { MainActivity().MySecondaryBottomAppBar() }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                ProfileScreen()
            }
        }
    }
}
