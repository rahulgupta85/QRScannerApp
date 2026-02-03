package com.example.appdev.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdev.data.local.QrEntity
import com.example.appdev.data.local.QrType
import com.example.appdev.data.repository.QrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: QrRepository
) : ViewModel() {

    private val _filterType = MutableStateFlow<QrType?>(null)
    
    val historyState: StateFlow<HistoryUiState> = combine(
        repository.getAllQrCodes(),
        _filterType
    ) { allItems, filter ->
        val filteredList = if (filter == null) {
            allItems
        } else {
            allItems.filter { it.type == filter }
        }
        
        // Calculate analytics
        val scannedCount = allItems.count { it.type == QrType.SCANNED }
        val generatedCount = allItems.count { it.type == QrType.GENERATED }
        
        HistoryUiState(
            items = filteredList,
            selectedFilter = filter,
            scannedCount = scannedCount,
            generatedCount = generatedCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState()
    )

    fun setFilter(type: QrType?) {
        _filterType.value = type
    }

    fun deleteItem(item: QrEntity) {
        viewModelScope.launch {
            repository.deleteQrCode(item)
        }
    }
}

data class HistoryUiState(
    val items: List<QrEntity> = emptyList(),
    val selectedFilter: QrType? = null,
    val scannedCount: Int = 0,
    val generatedCount: Int = 0
)
