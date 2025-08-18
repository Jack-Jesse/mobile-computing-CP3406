//package com.example.mystudyapp.api.model
//
//import com.example.mystudyapp.api.baseUrl
//import com.example.mystudyapp.api.geminiApi
//import retrofit2.awaitResponse
//import com.example.mystudyapp.api.model.GeminiRequest
//import com.example.mystudyapp.api.model.Candidate
//
//
//// Testing
//
//// Define your GeminiRequest and GeminiResponse data classes if they aren't already
//// These are simplified examples; adjust them to match the actual API structure.
////data class GeminiRequest(val contents: List<GeminiContent>) // Or whatever your request needs
////{
////    annotation class Part(val text: String)
////}
//
////data class Content(val parts: List<GeminiPart>)
////data class Part(val text: String)
////
////data class GeminiApiResponse( // Example structure, adjust to the actual response
////    val candidates: List<Candidate>?,
////    // Add other fields if necessary
////)
//
////data class Candidate(
////    val content: Content?,
////    // Add other fields
////)
//// Content and Part can be reused from the request or be specific to the response
//
//suspend fun makeGeminiRequestWithCoroutine(
//    request: GeminiRequest // Pass the fully formed request
//): String? { // Return the reply string directly (or a Result<String> for better error handling)
//    return try {
//        val response = geminiApi.generateContent(
//            body = request,
//            url = baseUrl
//        ).awaitResponse() // Call Retrofit suspend function
//
//        if (response.isSuccessful) {
//            val geminiResponse = response.body()
//            val reply = geminiResponse
//                ?.candidates?.firstOrNull()
//                ?.content?.parts?.firstOrNull()
//                ?.text
//            println("Gemini says: $reply")
//            reply
//        } else {
//            val errorBody = response.errorBody()?.string()
//            println("Error: ${response.code()} - $errorBody")
//            null
//        }
//    } catch (e: Exception) {
//        println("Request failed: ${e.message}")
//        null
//    }
//}
//
//
//
//
//
//
//
//
//
//
////fun makeGeminiRequestWithCoroutine(
////    generativeModel: String, // Changed type here
////    prompt: String, // Simplified request to a prompt string
////    request: GeminiRequest,
////
////    // Testing
////    onReplyReceived: (String?) -> Unit
////
////) {
////    // You'd call this from a CoroutineScope, e.g., viewModelScope
////    CoroutineScope(Dispatchers.IO).launch { // Example scope
////        try {
////            val response = geminiApi.generateContent(endpoint, request)
////            val retrofitResponse = response.awaitResponse() // Renamed for clarity
////            if (retrofitResponse.isSuccessful) {
//////                val geminiResponse = retrofitResponse.body()
//////                val reply = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
////                // Testing
////                val geminiResponse = retrofitResponse.body()
////                val reply = geminiResponse
////                    ?.candidates?.firstOrNull()
////                    ?.content?.parts?.firstOrNull()
////                    ?.text
////
////
////                println("Gemini says: $reply")
////                // Testing
////                onReplyReceived(reply as String?) // Call the callback with the reply
////
////
////            } else {
////                // Handle API error (e.g., invalid API key, malformed request)
////                println("Error: ${retrofitResponse.code()} - ${retrofitResponse.errorBody()?.string()}")
////            }
////        } catch (e: Exception) {
////            println("Request failed: ${e.message}")
////            // Handle more specific exceptions from the SDK if needed
////        }
////    }
////}