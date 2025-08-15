import android.content.Context
import android.net.Uri
import com.example.mystudyapp.api.model.GeminiPart
import com.example.mystudyapp.api.model.GeminiResponse
import com.example.mystudyapp.api.retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

//val client = val client = OkHttpClient()

fun convertPdfToText(context: Context, fileUri: Uri) {

    val client = OkHttpClient()
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(fileUri)
    val fileBytes = inputStream?.readBytes() ?: return

    // Create request body for PDF file
    val fileBody = fileBytes.toRequestBody("application/pdf".toMediaTypeOrNull())

    // Multipart form
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", "document.pdf", fileBody)
        .build()

    // Build request
    val request = Request.Builder()
        .url("https://pdf-converter-api.p.rapidapi.com/PdfToText?startPage=0&endPage=0")
        .post(requestBody)
        .addHeader("x-rapidapi-key", "986e213179msh2533c7af66dbb7ep1de88fjsn0e408c264928")
        .addHeader("x-rapidapi-host", "pdf-converter-api.p.rapidapi.com")
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build()

//    val response = client.newCall(request).execute()

    CoroutineScope(Dispatchers.IO).launch {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val extractedText = response.body?.string()
            println("Extracted: $extractedText")
            val reply = GeminiPart(text = "Summarise this into study flash cards: $extractedText")
            println("Gemini Reply: $reply")
        } else {
            println("Error: ${response.code} - ${response.body?.string()}")
        }
    }.start()
}
