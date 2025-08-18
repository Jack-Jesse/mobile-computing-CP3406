package com.example.mystudyapp.api

import com.example.mystudyapp.api.model.GeminiRequest
import com.example.mystudyapp.api.model.GeminiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface GeminiApi {
    @Headers("Content-Type: application/json")
    @POST
    fun generateContent(
        @Url url: String,
        @Body body: GeminiRequest
    ): Call<GeminiResponse>
}
