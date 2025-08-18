package com.example.mystudyapp.flashcards

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // Good practice for launching coroutines in ViewModel
import com.example.mystudyapp.mediaupload.convertPdfToTextAndGenerateFlashcards // << 1. IMPORT THE CORRECT FUNCTION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch // For launching coroutines

class FlashcardViewModel : ViewModel() {
    private val _flashcards = MutableStateFlow<List<String>>(emptyList())
    val flashcards: StateFlow<List<String>> = _flashcards

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun processPdfAndGenerateFlashcards(context: Context, uri: Uri) {
        _isLoading.value = true
        _error.value = null // Clear previous error

        convertPdfToTextAndGenerateFlashcards( // << 2. USE THE CORRECT FUNCTION NAME
            context = context.applicationContext, // Use application context if possible for long operations
            fileUri = uri,
            onFlashcardsReady = { cards ->
                _flashcards.value = cards
                _isLoading.value = false
            },
            onError = { errorMessage ->
                _error.value = errorMessage
                _isLoading.value = false
                // Optionally clear flashcards or handle error state for flashcards
                // _flashcards.value = emptyList()
            }
        )
    }

    fun clearError() {
        _error.value = null
    }
}
