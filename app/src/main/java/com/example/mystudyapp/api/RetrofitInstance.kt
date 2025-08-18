package com.example.mystudyapp.api


import com.example.mystudyapp.api.model.GeminiRequest
import com.example.mystudyapp.api.model.GeminiResponse // Make sure this is defined
import retrofit2.Response // Retrofit's Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

object RetrofitInstance {
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/" // Example, verify correct URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // This 'api' is what you use: RetrofitInstance.api.generateContent(...)
    val api: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }
}

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