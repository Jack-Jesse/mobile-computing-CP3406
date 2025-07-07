package com.example.mystudyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystudyapp.ui.theme.MyStudyAppTheme
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStudyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )
                        MySignInSection(modifier = Modifier.padding(innerPadding))
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

    @Composable
    fun MySignInSection(modifier: Modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
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
        }
    }
}


//    @Preview(showBackground = true)
//    @Composable
//    fun MySignInSectionPreview() {
//        Column(modifier = Modifier.padding(16.dp)) {
//            TextField(
//                value = "",
//                onValueChange = {},
//                label = { Text("Email") },
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            TextField(
//                value = "",
//                onValueChange = {},
//                label = { Text("Password") },
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//        }
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTopAppBarPreview() {
//    TopAppBar(
//        title = { Text("My Study App") },
//        actions = {
//            Icon(
//                imageVector = Icons.Rounded.Menu,
//                contentDescription = "Menu"
//            )
//        }
//    )
//}

//@Composable
//fun GreetingPreview() {
//    MyStudyAppTheme {
//        Greeting("Android")
//    }
//}