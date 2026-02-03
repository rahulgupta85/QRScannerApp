package com.example.appdev.ui.screens

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdev.data.local.QrEntity
import com.example.appdev.data.local.QrType
import com.example.appdev.data.repository.QrRepository
import com.example.appdev.utils.QrUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QrRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onScanResult(content: String) {
        if (_uiState.value.scannedContent == content) return // Debounce duplicate emission from camera
        
        val isUrl = android.util.Patterns.WEB_URL.matcher(content).matches()
        _uiState.value = _uiState.value.copy(
            scannedContent = content,
            showResultDialog = true,
            isUrl = isUrl
        )
        saveQrToHistory(content, QrType.SCANNED)
    }

    fun onGenerateClick(content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            val bitmap = QrUtils.generateQrBitmap(content)
            _uiState.value = _uiState.value.copy(
                generatedContent = content,
                generatedBitmap = bitmap,
                showResultDialog = true,
                isUrl = false // Generated content not treated as auto-open context in this flow usually
            )
            // Save generated QR
            if (bitmap != null) {
                saveQrToHistory(content, QrType.GENERATED)
            }
        }
    }

    fun onDismissDialog() {
        _uiState.value = _uiState.value.copy(showResultDialog = false, scannedContent = null, isUrl = false)
    }

    private fun saveQrToHistory(content: String, type: QrType) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertQrCode(
                QrEntity(
                    content = content,
                    type = type,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
    
    fun clearGeneratedResult() {
         _uiState.value = _uiState.value.copy(generatedBitmap = null, generatedContent = "")
    }
}

data class HomeUiState(
    val scannedContent: String? = null,
    val generatedContent: String = "",
    val generatedBitmap: Bitmap? = null,
    val showResultDialog: Boolean = false,
    val isUrl: Boolean = false
)
