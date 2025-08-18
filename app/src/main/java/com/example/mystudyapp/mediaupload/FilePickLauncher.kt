////package com.example.mystudyapp.mediaupload
////
////import android.content.Context
////import android.net.Uri
////import com.example.mystudyapp.api.model.GeminiContent
////import com.example.mystudyapp.api.model.GeminiPart
////import com.google.gson.Gson
////import kotlinx.coroutines.CoroutineScope
////import kotlinx.coroutines.Dispatchers
////import kotlinx.coroutines.launch
////import kotlinx.coroutines.withContext
////import okhttp3.MediaType.Companion.toMediaTypeOrNull
////import okhttp3.MultipartBody
////import okhttp3.OkHttpClient
////import okhttp3.Request
////import okhttp3.RequestBody.Companion.toRequestBody
////import com.example.mystudyapp.api.model.GeminiRequest
////import kotlin.jvm.java
////import com.example.mystudyapp.api.model.GeminiContent as api
////
////
////
////
////
////
////
////
////
////
////
////
//////val client = val client = OkHttpClient()
////
////// OG Code
////
//////fun convertPdfToText(context: Context, fileUri: Uri) {
//////
//////    val client = OkHttpClient()
//////    val contentResolver = context.contentResolver
//////    val inputStream = contentResolver.openInputStream(fileUri)
//////    val fileBytes = inputStream?.readBytes() ?: return
//////
//////    // Create request body for PDF file
//////    val fileBody = fileBytes.toRequestBody("application/pdf".toMediaTypeOrNull())
//////
//////    // Multipart form
//////    val requestBody = MultipartBody.Builder()
//////        .setType(MultipartBody.FORM)
//////        .addFormDataPart("file", "document.pdf", fileBody)
//////        .build()
//////
//////    // Build request
//////    val request = Request.Builder()
//////        .url("https://pdf-converter-api.p.rapidapi.com/PdfToText?startPage=0&endPage=0")
//////        .post(requestBody)
//////        .addHeader("x-rapidapi-key", "986e213179msh2533c7af66dbb7ep1de88fjsn0e408c264928")
//////        .addHeader("x-rapidapi-host", "pdf-converter-api.p.rapidapi.com")
//////        .addHeader("Content-Type", "application/x-www-form-urlencoded")
//////        .build()
//////
//////    CoroutineScope(Dispatchers.IO).launch {
//////        val response = client.newCall(request).execute()
//////        if (response.isSuccessful) {
//////            val extractedText = response.body?.string()
//////            println("Extracted: $extractedText")
//////            val reply = GeminiPart(text = "Summarise this into study flash cards: $extractedText")
//////            println("Gemini Reply: $reply")
//////        } else {
//////            println("Error: ${response.code} - ${response.body?.string()}")
//////        }
//////    }.start()
//////
//////}
////
////
////
////// New
////fun convertPdfToText(
////    context: Context,
////    fileUri: Uri,
////    onFlashcardsReady: (List<String>) -> Unit
////) {
////    val client = OkHttpClient()
////    val contentResolver = context.contentResolver
////    val inputStream = contentResolver.openInputStream(fileUri)
////    val fileBytes = inputStream?.readBytes() ?: return
////
////    val fileBody = fileBytes.toRequestBody("application/pdf".toMediaTypeOrNull())
////    val requestBody = MultipartBody.Builder()
////        .setType(MultipartBody.FORM)
////        .addFormDataPart("file", "document.pdf", fileBody)
////        .build()
////
////    val request = Request.Builder()
////        .url("https://pdf-converter-api.p.rapidapi.com/PdfToText?startPage=0&endPage=0")
////        .post(requestBody)
////        .addHeader("x-rapidapi-key", "986e213179msh2533c7af66dbb7ep1de88fjsn0e408c264928")
////        .addHeader("x-rapidapi-host", "pdf-converter-api.p.rapidapi.com")
////        .addHeader("Content-Type", "application/x-www-form-urlencoded")
////        .build()
////
////    CoroutineScope(Dispatchers.IO).launch {
////        val response = client.newCall(request).execute()
////        if (response.isSuccessful) {
////            val jsonText = response.body?.string()
////            println("Extracted JSON: $jsonText")
////
////            // Parse JSON into FlashcardResponse
////            val gson = Gson()
////            val flashcardData = gson.fromJson(jsonText, FlashcardResponse::class.java)
////
////            // Gemini prompt construction
////            val extractedText =
////                    flashcardData.data?.joinToString("\n") { page ->
////                        page.content?.replace("\r\n", "\n")?.trim() ?: ""
////                    }
////            val prompt = GeminiPart(text = "Summarise this into study flash cards: $extractedText")
//////            val geminiRequest = GeminiRequest(contents = listOf(prompt))
////            val geminiRequest = GeminiRequest(contents = listOf(GeminiContent(parts = listOf(prompt))))
////
////            // Call Gemini API
//////            val geminiApiResponse = api.generateContent(geminiRequest)
////            val geminiApiResponse = api.generateContent(geminiRequest)
////
////            if (geminiApiResponse.isSuccessful) {
////                val geminiResponse = geminiApiResponse.body()
////                val generatedText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
////                if (generatedText != null) {
////                    // Assuming Gemini returns flashcards in "Title::Content\nTitle::Content" format
////                    val flashcards = generatedText.split("\n").filter { it.contains("::") }
////                    // Return to UI on main thread
////                    withContext(Dispatchers.Main) {
////                        println("Generated Flashcards: ${flashcards.joinToString("\n\n")}")
////                        onFlashcardsReady(flashcards)
////                    }
////                } else {
////                    println("Error: Gemini response text is null")
////                }
////            } else {
////                println("Error calling Gemini API: ${geminiApiResponse.code()} - ${geminiApiResponse.errorBody()?.string()}")
////            }
////        } else {
////            println("Error: ${response.code} - ${response.body?.string()}")
////        }
////    }
////}
////
////data class FlashcardResponse(
////    val status: String?,
////    val data: List<FlashcardPage>?
////)
////
////data class FlashcardPage(
////    val pageNo: Int?,
////    val content: String?
////)
////
////
//
//
//// File: app/src/main/java/com/example/mystudyapp/mediaupload/FilePickLauncher.kt
//// (Ensure your package name matches your project structure)
//package com.example.mystudyapp.mediaupload // Or your actual package
//
//import android.content.Context
//import android.net.Uri
//import com.example.mystudyapp.api.geminiService // << IMPORT our mailman
//import com.example.mystudyapp.api.model.GeminiContent
//import com.example.mystudyapp.api.model.GeminiPart
//import com.example.mystudyapp.api.model.GeminiRequest
//// We don't directly use GeminiResponse here, but geminiService expects it.
//import com.google.gson.Gson
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody.Companion.toRequestBody
//
//// Data classes for parsing the PDF-to-Text API response (these are specific to that other service)
//data class FlashcardResponseFromPdfService( // Renamed to avoid confusion
//    val status: String?,
//    val data: List<FlashcardPageFromPdfService>? // Renamed
//)
//
//data class FlashcardPageFromPdfService( // Renamed
//    val pageNo: Int?,
//    val content: String?
//)
//
//fun convertPdfToTextAndGenerateFlashcards(
//    context: Context,
//    fileUri: Uri,
//    onFlashcardsReady: (List<String>) -> Unit,
//    onError: (String) -> Unit
//) {
//    val pdfToTextClient = OkHttpClient()
//    val contentResolver = context.contentResolver
//
//    CoroutineScope(Dispatchers.IO).launch {
//        // --- Stage 1: Get text from the PDF (using the PDF-to-Text cloud service) ---
//        val inputStream = contentResolver.openInputStream(fileUri)
//        val fileBytes = try {
//            inputStream?.readBytes()
//        } catch (e: Exception) {
//            println("Error reading file bytes: ${e.message}")
//            withContext(Dispatchers.Main) { onError("Error reading PDF file.") }
//            return@launch
//        } finally {
//            try {
//                inputStream?.close()
//            } catch (e: Exception) { /* Ignored */
//            }
//        }
//
//        if (fileBytes == null) {
//            withContext(Dispatchers.Main) { onError("Could not read PDF file.") }
//            return@launch
//        }
//
//        val fileRequestBody = fileBytes.toRequestBody("application/pdf".toMediaTypeOrNull())
//        val pdfMultipartBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("file", "document.pdf", fileRequestBody)
//            .build()
//
//        // TODO: Replace with your actual RapidAPI key for PDF conversion
//        val pdfApiRequest = Request.Builder()
//            .url("https://pdf-converter-api.p.rapidapi.com/PdfToText?startPage=0&endPage=0")
//            .post(pdfMultipartBody)
//            .addHeader("x-rapidapi-key", "YOUR_RAPIDAPI_KEY_FOR_PDF_CONVERTER")
//            .addHeader("x-rapidapi-host", "pdf-converter-api.p.rapidapi.com")
//            .build()
//
//        val pdfApiResponse = try {
//            pdfToTextClient.newCall(pdfApiRequest).execute()
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) { onError("Failed to connect to PDF converter.") }
//            return@launch
//        }
//
//        if (!pdfApiResponse.isSuccessful) {
//            val errorBody = pdfApiResponse.body?.string()
//            pdfApiResponse.body?.close()
//            withContext(Dispatchers.Main) { onError("PDF converter service failed: ${pdfApiResponse.code} $errorBody") }
//            return@launch
//        }
//
//        val jsonTextFromPdfService = pdfApiResponse.body?.string()
//        pdfApiResponse.body?.close()
//
//        if (jsonTextFromPdfService == null) {
//            withContext(Dispatchers.Main) { onError("PDF converter returned empty response.") }
//            return@launch
//        }
//
//        val gson = Gson()
//        val pdfTextData = try {
//            gson.fromJson(jsonTextFromPdfService, FlashcardResponseFromPdfService::class.java)
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) { onError("Invalid response from PDF converter.") }
//            return@launch
//        }
//
//        val extractedPdfText = pdfTextData.data?.joinToString("\n\n") { page ->
//            page.content?.replace("\r\n", "\n")?.trim() ?: ""
//        }?.trim()
//
//        if (extractedPdfText.isNullOrBlank()) {
//            withContext(Dispatchers.Main) {
//                // You could choose to show an error or just say "no text, so no flashcards"
//                onError("No text was found in the PDF to make flashcards from.")
//                // onFlashcardsReady(emptyList())
//            }
//            return@launch
//        }
//
//        // --- Stage 2: Prepare the letter (prompt) for Gemini AI ---
//        val promptForGemini = """
//            Please create study flashcards from the following text.
//            For each flashcard, provide a "Front:" with a question or term, and a "Back:" with the answer or definition.
//            Separate each complete flashcard (Front and Back) with two newlines if possible.
//
//            Text to use:
//            "$extractedPdfText"
//        """.trimIndent()
//
//        // Create the structured request for Gemini using our data classes
//        val geminiPart = GeminiPart(text = promptForGemini)
//        val geminiContent = GeminiContent(parts = listOf(geminiPart)) // role is "user" by default
//        val geminiApiRequest = GeminiRequest(contents = listOf(geminiContent))
//
//        // --- Stage 3: Send the letter to Gemini AI using our mailman (geminiService) ---
//        try {
//            // Our mailman (geminiService) takes the letter (geminiApiRequest)
//            // The API key is handled inside geminiService as per GeminiApiSetup.kt
//            val responseFromGemini = geminiService.generateContent(request = geminiApiRequest)
//
//            if (responseFromGemini.isSuccessful) {
//                val geminiResponseBody = responseFromGemini.body()
//                val generatedTextFromGemini =
//                    geminiResponseBody?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
//
//                if (generatedTextFromGemini != null) {
//                    // Simple parsing for "Front: ... Back: ..."
//                    // This parsing might need to be improved based on Gemini's actual output.
//                    val parsedFlashcards =
//                        generatedTextFromGemini.splitToSequence(Regex("\n *(?:Front:|Back:)"))
//                            .map { it.trim() }
//                            .filter { it.isNotEmpty() }
//                            .chunked(2) // Group into pairs of (hopefully) Front and Back
//                            .mapNotNull { pair ->
//                                if (pair.size == 2) {
//                                    val frontText = pair[0]
//                                    val backText = pair[1]
//                                    "Front: $frontText\nBack: $backText"
//                                } else if (pair.size == 1 && pair[0].contains(":")) {
//                                    // If only one part found but looks like a flashcard line
//                                    pair[0]
//                                } else {
//                                    null // Ignore malformed pairs
//                                }
//                            }
//                            .toList()
//
//
//                    withContext(Dispatchers.Main) {
//                        if (parsedFlashcards.isNotEmpty()) {
//                            onFlashcardsReady(parsedFlashcards)
//                        } else {
//                            onError("Gemini AI responded, but flashcards could not be understood. Raw: $generatedTextFromGemini")
//                        }
//                    }
//                } else {
//                    withContext(Dispatchers.Main) { onError("Gemini AI returned an empty or unexpected answer.") }
//                }
//            } else {
//                val errorBody = responseFromGemini.errorBody()?.string()
//                responseFromGemini.errorBody()?.close()
//                withContext(Dispatchers.Main) { onError("Gemini AI service error: ${responseFromGemini.code()} - $errorBody") }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace() // For debugging
//            withContext(Dispatchers.Main) { onError("Could not talk to Gemini AI helper: ${e.message}") }
//        }
//    }
//}
//
//
//// --- Place your RetrofitInstance.kt and GeminiApiService interface here or in your api package ---
//// Example (ensure this is in your project, e.g., in com/example/mystudyapp/api/RetrofitInstance.kt):
///*
//package com.example.mystudyapp.api
//
//import com.example.mystudyapp.api.model.GeminiRequest
//import com.example.mystudyapp.api.model.GeminiResponse // Make sure this is defined
//import retrofit2.Response // Retrofit's Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.Body
//import retrofit2.http.POST
//import retrofit2.http.Query
//
//object RetrofitInstance {
//    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/" // Example, verify correct URL
//
//    private val retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(GEMINI_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    // This 'api' is what you use: RetrofitInstance.api.generateContent(...)
//    val api: GeminiApiService by lazy {
//        retrofit.create(GeminiApiService::class.java)
//    }
//}
//
//interface GeminiApiService {
//    // The path and API key query parameter depend on the specific Gemini API version you're using.
//    // For Vertex AI, it's different. For the new Google AI Studio (Gemini API), it's usually v1beta.
//    // Example for Google AI Studio Gemini API:
//    @POST("v1beta/models/gemini-pro:generateContent") // Replace gemini-pro with your model
//    suspend fun generateContent(
//        @Body request: GeminiRequest,
//        @Query("key") apiKey: String = "YOUR_GEMINI_API_KEY" // TODO: SECURE THIS KEY
//    ): Response<GeminiResponse> // Retrofit's Response wrapping your data class
//}
//*/
//
//
