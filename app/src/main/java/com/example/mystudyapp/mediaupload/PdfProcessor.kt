package com.example.mystudyapp.mediaupload


import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.mystudyapp.api.geminiService
import com.example.mystudyapp.api.model.GeminiContent
import com.example.mystudyapp.api.model.GeminiPart
import com.example.mystudyapp.api.model.GeminiRequest
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

// Data classes for parsing the PDF-to-Text API response
data class FlashcardResponseFromPdfService(
    val status: String?,
    val data: List<FlashcardPageFromPdfService>?
)

data class FlashcardPageFromPdfService(
    val pageNo: Int?,
    val content: String?
)

fun convertPdfToTextAndGenerateFlashcards(
    context: Context, // Parameter 1: Context
    fileUri: Uri,     // Parameter 2: Uri
    onFlashcardsReady: (List<String>) -> Unit, // Parameter 3: Lambda
    onError: (String) -> Unit                  // Parameter 4: Lambda
) {
    val pdfToTextClient = OkHttpClient()
    val contentResolver = context.contentResolver

    CoroutineScope(Dispatchers.IO).launch {
        // --- Stage 1: Get text from the PDF ---
        val inputStream = contentResolver.openInputStream(fileUri)
        val fileBytes = try {
            inputStream?.readBytes()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { onError("Error reading PDF: ${e.message}") }
            return@launch
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) { /* Ignored */ }
        }

        if (fileBytes == null) {
            withContext(Dispatchers.Main) { onError("Could not read PDF file.") }
            return@launch
        }

        val fileRequestBody = fileBytes.toRequestBody("application/pdf".toMediaTypeOrNull())
        val pdfMultipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "document.pdf", fileRequestBody)
            .build()

        // TODO: IMPORTANT! Replace with your RapidAPI key for PDF-to-Text service
        val pdfRapidApiKey = "986e213179msh2533c7af66dbb7ep1de88fjsn0e408c264928"
        if (pdfRapidApiKey == "API_KEY_HERE") {
            withContext(Dispatchers.Main) { onError("PDF Converter API Key not set.") }
            return@launch
        }

        val pdfApiRequest = Request.Builder()
            .url("https://pdf-converter-api.p.rapidapi.com/PdfToText?startPage=0&endPage=0")
            .post(pdfMultipartBody)
            .addHeader("x-rapidapi-key", pdfRapidApiKey)
            .addHeader("x-rapidapi-host", "pdf-converter-api.p.rapidapi.com")
            .build()


        // OG
//        val pdfApiResponse = try {
//            pdfToTextClient.newCall(pdfApiRequest).execute()
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) { onError("PDF Service Connection: ${e.message}") }
//            return@launch
//        }
//
//        if (!pdfApiResponse.isSuccessful) {
//            val errorBody = pdfApiResponse.body?.string()
//            pdfApiResponse.body?.close()
//            withContext(Dispatchers.Main) { onError("PDF Service Error ${pdfApiResponse.code}: $errorBody") }
//            return@launch
//        }
//
//        val jsonTextFromPdfService = pdfApiResponse.body?.string()
//        pdfApiResponse.body?.close()
//        if (jsonTextFromPdfService == null) {
//            withContext(Dispatchers.Main) { onError("PDF Service: Empty response.") }
//            return@launch
//        }
//
//        val gson = Gson()
//        val pdfTextData = try {
//            gson.fromJson(jsonTextFromPdfService, FlashcardResponseFromPdfService::class.java)
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) { onError("PDF Service: Bad JSON - ${e.message}") }
//            return@launch
//        }

        // Test code
        // ... (code for pdfMultipartBody and pdfApiRequest is ABOVE this) ...

        Log.d("PdfProcessor_Debug", "STAGE 1: Calling RapidAPI PDF-to-Text service...") // DEBUG Line

        val pdfApiResponse = try {
            pdfToTextClient.newCall(pdfApiRequest).execute()
        } catch (e: Exception) {
            Log.e("PdfProcessor_Debug", "STAGE 1: RapidAPI Call Exception: ${e.message}", e) // DEBUG Line
            withContext(Dispatchers.Main) { onError("PDF Service Connection Error: ${e.message}") }
            return@launch
        }

        // IMPORTANT: Read the body ONCE and store it, then log it.
        val responseBodyString = try {
            pdfApiResponse.body?.string() // This consumes the body
        } catch (e: Exception) {
            Log.e("PdfProcessor_Debug", "STAGE 1: Error reading RapidAPI response body: ${e.message}", e) // DEBUG Line
            withContext(Dispatchers.Main) { onError("PDF Service: Error reading response - ${e.message}") }
            pdfApiResponse.body?.close() // Ensure it's closed on error
            return@launch
        }

        Log.d("PdfProcessor_Debug", "STAGE 1: RapidAPI Response Code: ${pdfApiResponse.code}") // DEBUG Line
        Log.d("PdfProcessor_Debug", "STAGE 1: Raw RapidAPI Response Body: $responseBodyString") // DEBUG Line

        if (!pdfApiResponse.isSuccessful) {
            // pdfApiResponse.body?.close() // Body already consumed or closed
            withContext(Dispatchers.Main) { onError("PDF Service Error ${pdfApiResponse.code}: $responseBodyString") }
            return@launch
        }

        if (responseBodyString.isNullOrBlank()) { // Check if it's blank too
            Log.w("PdfProcessor_Debug", "STAGE 1: RapidAPI returned an empty or null response body.") // DEBUG Line
            withContext(Dispatchers.Main) { onError("PDF Service: Empty response from server.") }
            return@launch
        }

        val gson = Gson()
        val pdfTextData = try {
            gson.fromJson(responseBodyString, FlashcardResponseFromPdfService::class.java)
        } catch (e: Exception) {
            Log.e("PdfProcessor_Debug", "STAGE 1: RapidAPI JSON Parsing Exception: ${e.message}. JSON was: $responseBodyString", e) // DEBUG Line with problematic JSON
            withContext(Dispatchers.Main) { onError("PDF Service: Could not understand response (Bad JSON) - ${e.message}") }
            return@launch
        }



        val extractedPdfText = pdfTextData.data?.joinToString("\n\n") { page ->
            page.content?.replace("\r\n", "\n")?.trim() ?: ""
        }?.trim()

        if (extractedPdfText.isNullOrBlank()) {
            withContext(Dispatchers.Main) { onError("No text found in PDF.") }
            return@launch
        }

        // --- Stage 2: Prepare prompt for Gemini AI ---
        val promptForGemini = """
            Create study flashcards from the following text.
            For each flashcard, provide "Q:" with a question/term, and "A:" with the answer/definition.
            Separate each complete flashcard (Q and A) with two newlines.

            Text:
            "$extractedPdfText"
        """.trimIndent()

        val geminiPart = GeminiPart(text = promptForGemini)
        val geminiContent = GeminiContent(parts = listOf(geminiPart))
        val geminiApiRequest = GeminiRequest(contents = listOf(geminiContent))

        // --- Stage 3: Send to Gemini AI ---
        try {
            if (com.example.mystudyapp.api.GEMINI_API_KEY == "API_KEY_HERE") {
                withContext(Dispatchers.Main) { onError("Gemini API Key not set.") }
                return@launch
            }
            val responseFromGemini = geminiService.generateContent(request = geminiApiRequest)

            if (responseFromGemini.isSuccessful) {
                val geminiResponseBody = responseFromGemini.body()
                val generatedText = geminiResponseBody?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

                if (generatedText != null) {
                    val parsedFlashcards = generatedText.splitToSequence(Regex("\n *(?:Q:|A:)"))
                        .map { it.trim().removePrefix("Q:").removePrefix("A:").trim() }
                        .filter { it.isNotEmpty() }
                        .chunked(2)
                        .mapNotNull { pair ->
                            if (pair.size == 2) "Q: ${pair[0]}\nA: ${pair[1]}" else null
                        }.toList()

                    withContext(Dispatchers.Main) {
                        if (parsedFlashcards.isNotEmpty()) onFlashcardsReady(parsedFlashcards)
                        else onError("Gemini made no flashcards. Raw: $generatedText")
                    }
                } else {
                    withContext(Dispatchers.Main) { onError("Gemini: Empty or unclear answer.") }
                }
            } else {
                val errorBody = responseFromGemini.errorBody()?.string()
                responseFromGemini.errorBody()?.close()
                withContext(Dispatchers.Main) { onError("Gemini Error ${responseFromGemini.code()}: $errorBody") }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { onError("Gemini Connection: ${e.message}") }
        }
    }
}
