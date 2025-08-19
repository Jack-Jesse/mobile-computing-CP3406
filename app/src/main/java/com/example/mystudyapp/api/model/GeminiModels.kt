package com.example.mystudyapp.api.model

// --- Request to Gemini ---
data class GeminiRequest(
    val contents: List<GeminiContent>
)

data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String? = "user"
)

data class GeminiPart(
    val text: String
)

// --- Response from Gemini ---
data class GeminiResponse(
    val candidates: List<Candidate>?,
    val promptFeedback: PromptFeedback?
)

data class Candidate(
    val content: GeminiContent?,
    val finishReason: String?,
    val index: Int?,
    val safetyRatings: List<SafetyRating>?
)

data class PromptFeedback(
    val safetyRatings: List<SafetyRating>?
)

data class SafetyRating(
    val category: String?,
    val probability: String?
)