package com.example.mystudyapp.api.model

import androidx.compose.ui.semantics.text
import com.example.mystudyapp.api.endpoint
import com.example.mystudyapp.api.geminiApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse


fun makeGeminiRequestWithCoroutine(
    generativeModel: String, // Changed type here
    prompt: String, // Simplified request to a prompt string
    request: GeminiRequest
) {
    // You'd call this from a CoroutineScope, e.g., viewModelScope
    CoroutineScope(Dispatchers.IO).launch { // Example scope
        try {
            val response = geminiApi.generateContent(endpoint, request)
            val retrofitResponse = response.awaitResponse() // Renamed for clarity
            if (retrofitResponse.isSuccessful) {
                val geminiResponse = retrofitResponse.body()
                val reply = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                println("Gemini says: $reply")
            } else {
                // Handle API error (e.g., invalid API key, malformed request)
                println("Error: ${retrofitResponse.code()} - ${retrofitResponse.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("Request failed: ${e.message}")
            // Handle more specific exceptions from the SDK if needed
        }
    }
}