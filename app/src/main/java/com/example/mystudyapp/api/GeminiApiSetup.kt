package com.example.mystudyapp.api

import com.example.mystudyapp.api.model.GeminiRequest
import com.example.mystudyapp.api.model.GeminiResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

// --- Configuration Variables ---
private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"

// TODO: IMPORTANT! Replace with your actual key. Do not ship this in production code.
const val GEMINI_API_KEY = "AIzaSyAr1xVIAy6-FMGwH4_gx0w9HrrPysfYevM"

// --- Setting up the Mail Service (Retrofit) ---
private val geminiRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl(GEMINI_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// --- The Actual Letter Carrier (Your API Service Interface) ---
interface GeminiApiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent?key=$GEMINI_API_KEY") // You can change "gemini-pro"
    suspend fun generateContent(
        @Body request: GeminiRequest,
        @Query("key") apiKey: String = GEMINI_API_KEY
    ): Response<GeminiResponse>
}

// --- Making the Letter Carrier Ready to Use ---
val geminiService: GeminiApiService = geminiRetrofit.create(GeminiApiService::class.java)
