package com.example.mystudyapp.mediaupload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import convertPdfToText

// Upload PDF
@Composable
fun UploadPdfScreen() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            convertPdfToText(context, it)
        }
    }

    Button(onClick = {
        launcher.launch(arrayOf("application/pdf"))
    }) {
        Text("Upload PDF")
    }
}