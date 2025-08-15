package com.example.mystudyapp.api.model

data class GeminiPart(val text: String)
data class GeminiContent(val parts: List<GeminiPart>)
data class GeminiRequest(val contents: List<GeminiContent>)

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: GeminiContent?
)
