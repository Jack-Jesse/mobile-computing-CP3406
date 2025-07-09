package com.example.mystudyapp

import android.os.Bundle
import androidx.compose.material3.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object SignInScreen : Screen("sign_in_screen")
    // Add other screens here
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStudyAppTheme {
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
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                TextButton(onClick = { /* ToDo */ }) {
                    Text("Sign In")
                }
            }
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
        Column(
            modifier = Modifier.padding(16.dp).background(Color.LightGray).fillMaxWidth(),
            horizontalAlignment =  Alignment.CenterHorizontally

        ) {
            Text("Modal Date Picker")
            Icon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = "Calender"
            )
            Text("📅DATE: 12/12/2025\n🕐TIME: 12:00")
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

@Preview(showBackground = true)
@Composable
fun MyHomeScreenPreview() {
    MyStudyAppTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainActivity().MyTopAppBar()
                MainActivity().MyScheduleSection()
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun MySignInSectionPreview() {
//    MyStudyAppTheme {
//        Scaffold {
//            Column(
//                modifier = Modifier
//                    .padding(it)
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                MainActivity().Greeting(
//                    modifier = Modifier.padding(it)
//                )
//                MainActivity().MySignInSection(
//                    modifier = Modifier.padding(horizontal = 0.dp)
//                )
//            }
//        }
//    }
//}