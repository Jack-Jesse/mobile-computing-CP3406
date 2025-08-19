package com.example.mystudyapp.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/" // Example, verify correct URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }
}
