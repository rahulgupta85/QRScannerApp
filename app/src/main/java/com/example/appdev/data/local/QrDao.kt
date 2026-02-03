package com.example.appdev.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QrDao {
    @Query("SELECT * FROM qr_history ORDER BY timestamp DESC")
    fun getAllQrCodes(): Flow<List<QrEntity>>

    @Insert
    suspend fun insertQrCode(qrEntity: QrEntity)

    @Delete
    suspend fun deleteQrCode(qrEntity: QrEntity)
    
    @Query("SELECT * FROM qr_history WHERE type = :type ORDER BY timestamp DESC")
    fun getQrCodesByType(type: QrType): Flow<List<QrEntity>>
}
