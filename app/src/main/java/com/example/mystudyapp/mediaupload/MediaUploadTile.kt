import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystudyapp.flashcards.FlashcardViewModel

@Composable
fun MediaUploadScreen(viewModel: FlashcardViewModel = viewModel()) {
    val flashcards by viewModel.flashcards.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.processPdfAndGenerateFlashcards(context, it)
        }
    }


    Column {
        Button(onClick = {
            filePickerLauncher.launch("application/pdf")

//            val uri =  // use file picker launcher result
//            viewModel.processPdf(context, uri)
        }) {
            Text("Upload PDF")
        }

        // Show dialog when flashcards are available
        LaunchedEffect(flashcards) {
            if (flashcards.isNotEmpty()) showDialog = true
        }

        if (showDialog) {
            FlashcardDialog(flashcards = flashcards) {
                showDialog = false
            }
        }
    }
}



// testing
@Composable
fun FlashcardDialog(
    flashcards: List<String>,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text("Flashcard ${currentIndex + 1} of ${flashcards.size}")
        },
        text = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                val lines = flashcards[currentIndex].split("\n")
                lines.forEach { line ->
                    Text(text = line.trim())
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentIndex > 0) {
                    TextButton(onClick = { currentIndex-- }) {
                        Text("Previous")
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                if (currentIndex < flashcards.size - 1) {
                    TextButton(onClick = { currentIndex++ }) {
                        Text("Next")
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }
            }
        }
    )
}
