package com.example.appdev.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QrType {
    SCANNED,
    GENERATED
}

@Entity(tableName = "qr_history")
data class QrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val type: QrType,
    val timestamp: Long,
    val format: String = "" // e.g. "QR_CODE", "EAN_13"
)
