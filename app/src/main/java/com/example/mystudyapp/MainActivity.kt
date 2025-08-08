package com.example.mystudyapp

//import com.example.mystudyapp.ui.theme.MyStudyAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.compose.errorLight
import com.example.compose.onErrorDark
import com.example.compose.onErrorDarkHighContrast
import com.example.compose.onErrorLight
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryContainerLightHighContrast
import com.example.compose.onPrimaryLight
import com.example.compose.onTertiaryLight
import com.example.compose.primaryContainerLight
import com.example.compose.primaryContainerLightHighContrast
import java.text.SimpleDateFormat
import java.util.Date




class MainActivity : ComponentActivity() {
    // Moved darkTheme state to be a rememberable state within the root Composable
    // or managed by a ViewModel if it needs to survive configuration changes robustly.
    // For simplicity here, we'll manage it at the Ap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme = remember { mutableStateOf(false) }
            AppTheme(darkTheme = darkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavHost(darkTheme = darkTheme)
                }
            }
        }
    }
}

object Screen {
    const val SignIn = "signIn"
    const val Home = "home"
    const val Profile = "profile"
    const val Settings = "settings"
    const val MediaUpload = "mediaUpload"

    // placeholders used from ProfileScreen
    const val EditProfile = "editProfile"
    const val ChangePassword = "changePassword"
    const val NotificationSettings = "notificationSettings"
}

    @Composable
    fun MyAppNavHost(
        modifier: Modifier = Modifier,
        darkTheme: MutableState<Boolean>, // Receive darkTheme state
        navController: NavHostController = rememberNavController(),
        startDestination: String = Screen.SignIn
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(Screen.SignIn) {
                MySignInSection(modifier = Modifier, navController = navController)
            }
            composable(Screen.Home) { // Changed "home" to Screen.Home for consistency
                MyHomeScreen(
                    navController = navController,
                    modifier = Modifier.fillMaxSize() // Ensure MyHomeScreen fills the available space
                )
            }
            composable(Screen.Profile) {
                Scaffold(
                    topBar = { MyTopAppBar(navController) },
                    bottomBar = { MySecondaryBottomAppBar(navController) }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        ProfileScreen(navController = navController) // Example usage
                    }
                }
            }
            composable(Screen.Settings) {
                Scaffold(
                    topBar = { MyTopAppBar(navController) },
                    bottomBar = { MySecondaryBottomAppBar(navController) }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        SettingsScreen(darkTheme = darkTheme)
                    }
                }
            }

            composable(Screen.MediaUpload) {
                MediaUploadPage()
            }

            // ---- Placeholders for the profile options ----
//            composable(Screen.EditProfile) { CenterText("Edit Profile (todo)") }
//            composable(Screen.ChangePassword) { CenterText("Change Password (todo)") }
//            composable(Screen.NotificationSettings) { CenterText("Notification Settings (todo)") }
        }
    }

    @Composable
    private fun CenterText(msg: String) {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(msg) }
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
    fun MyTopAppBar(navController: NavHostController) {
        TopAppBar(
            title = { Text("Study Tok") },
            navigationIcon = {
                IconButton(onClick = { /* ToDo */ }) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Menu",
                        tint = onPrimaryLight
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle profile click */ }) {
                    Icon(
                        Icons.Rounded.Person,
                        contentDescription = "Profile",
                        tint = onPrimaryLight
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = onTertiaryLight,
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyModalDatePicker() {
        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()
        var selectedDate by remember { mutableStateOf<Long?>(null) }

        Button(onClick = { showDatePicker = true }) {
            Text("Set Date and Time")
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDate = datePickerState.selectedDateMillis
                        showDatePicker = false
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

        if (selectedDate != null) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = primaryContainerLight)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val formattedDate = SimpleDateFormat("dd/MM/yyyy").format(Date(selectedDate!!))
                    Text("📅DATE: $formattedDate\n🕐TIME: Not Set", // Time part is not handled by DatePicker
                        color = onPrimaryContainerLight
                    )
                }
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
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
//            border = BorderStroke(2.dp, Color.Gray),
            colors = CardDefaults.cardColors(containerColor = primaryContainerLight)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment =  Alignment.CenterHorizontally,
            ) {
                Text("Study Session")
                Icon(
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = "Calender"
                )
                Text("📅DATE: 12/12/2025\n🕐TIME: 12:00",
                    color = onPrimaryContainerLight)
            }
        }
    }

    @Composable
    fun MyBottomAppBar(navController1: NavHostController) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            IconButton(onClick = { /* doSomething() */ }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.Home, contentDescription = "Home")
            }
            IconButton(
                onClick = { /* doSomething() */ },
                modifier = Modifier
//                    .shadow(elevation = 4.dp, shape = CircleShape, clip = false) // Drop shadow
//                    .padding(0.dp)
                    .background(color = MaterialTheme.colorScheme.error, CircleShape) // Slightly lighter for highlight
                    .border(BorderStroke(3.dp, onErrorDarkHighContrast), CircleShape) // Darker border for 3D effect
                    .aspectRatio(1f) // Ensure the button remains circular
                    .padding(bottom = 4.dp)
            ) {
                Icon(Icons.Rounded.Add,
                    tint = onErrorDark,
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
    fun MySecondaryBottomAppBar(navController: NavHostController) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
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
    fun MySignInSection(modifier: Modifier, navController: NavHostController) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment =  Alignment.CenterHorizontally
        ) {
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

            MySignInButton { navController.navigate("home") }

            MyUnderLinedTextButton("Skip Sign In") { navController.navigate("home") }
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
fun ProfileScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .background(color = colorResource(id = R.color.white))
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
        ProfileOptionItem("Edit Profile") { navController.navigate("editProfile") /* Placeholder */ }
        ProfileOptionItem("Change Password") { navController.navigate("changePassword") /* Placeholder */ }
        ProfileOptionItem("Notification Settings") { navController.navigate("notificationSettings") /* Placeholder */ }

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
            .background(color = colorResource(id = R.color.white))
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
                containerColor = errorLight,
                contentColor = onErrorLight

            )
        ) {
            Text("Logout")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaUploadPage() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(topBar = { /* use parent app bar or add your own */ }) { innerPadding ->
            Column(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.white))
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
                    .padding(vertical = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(25.dp)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.pdf),
                        contentDescription = "Upload PDF",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(5.dp)
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
                    .padding(vertical = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(25.dp)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.mp3),
                        contentDescription = "Upload PDF",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(5.dp)
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
                    .padding(vertical = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(25.dp)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.mp4),
                        contentDescription = "Upload PDF",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(5.dp)
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
fun MyHomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MyTopAppBar(navController) },
        bottomBar = { MyBottomAppBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyScheduleSection()
            MyUpcomingSection()
        }
    }
}



//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun MediaUploadPagePreview() {
//    AppTheme  {
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            bottomBar = { MySecondaryBottomAppBar(navController) }
//        ) { innerPadding ->
//            Column(modifier = Modifier.padding(innerPadding)) {
//                MediaUploadPage()
//            }
//        }
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun MyHomeScreenPreview() {
//    val navController = rememberNavController()
//    AppTheme {
//        MyHomeScreen(navController = navController)
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun MySignInSectionPreview() {
//    AppTheme {
//        Scaffold {
//            val navController = rememberNavController()
//            Column(
//                modifier = Modifier
//                    .background(color = colorResource(id = R.color.white))
//                    .padding(it)
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Greeting(
//                    modifier = Modifier.padding(it)
//                )
//                MySignInSection(
//                    modifier = Modifier.padding(horizontal = 0.dp),
//                    navController = navController
//                )
//            }
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun SettingsScreenPreview() {
//    val darkTheme = remember { mutableStateOf(false) }
//    AppTheme(darkTheme = darkTheme.value) {
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            topBar = { MyTopAppBar(navController) },
//            bottomBar = { MySecondaryBottomAppBar(navController) }
//        ) { innerPadding ->
//            Column(modifier = Modifier
//                .padding(innerPadding)
//                .fillMaxSize()) {
//                SettingsScreen(darkTheme = darkTheme)
//            }
//
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    val darkTheme = remember { mutableStateOf(false) }
//    val navController = rememberNavController()
//    AppTheme(darkTheme = darkTheme.value) {
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            topBar = { MyTopAppBar(navController) },
//            bottomBar = { MySecondaryBottomAppBar(navController) }
//        ) { innerPadding ->
//            Column(
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//            ) {
//                ProfileScreen(navController = navController)
//            }
//        }
//    }
//}
