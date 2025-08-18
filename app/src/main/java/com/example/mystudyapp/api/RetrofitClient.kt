//package com.example.mystudyapp.api
//
//import com.example.mystudyapp.api.model.Content
//import com.example.mystudyapp.api.model.GeminiContent
//import com.example.mystudyapp.api.model.GeminiPart
//import com.example.mystudyapp.api.model.GeminiRequest
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//
//val apiKey = "AIzaSyAr1xVIAy6-FMGwH4_gx0w9HrrPysfYevM"  // replace this with your real key
//val baseUrl = "https://generativelanguage.googleapis.com/"
//val endpoint = "v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"
//
//val retrofit = Retrofit.Builder()
//    .baseUrl(baseUrl)
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()
//
//val geminiApi = retrofit.create(GeminiApi::class.java)
//
//data class GeminiRequest(val modelPrompt: Content) // Expects a single Content, not a List