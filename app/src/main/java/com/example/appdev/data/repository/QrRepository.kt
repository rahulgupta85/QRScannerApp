package com.example.appdev.data.repository

import com.example.appdev.data.local.QrDao
import com.example.appdev.data.local.QrEntity
import com.example.appdev.data.local.QrType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrRepository @Inject constructor(
    private val qrDao: QrDao
) {
    fun getAllQrCodes(): Flow<List<QrEntity>> = qrDao.getAllQrCodes()

    fun getQrCodesByType(type: QrType): Flow<List<QrEntity>> = qrDao.getQrCodesByType(type)

    suspend fun insertQrCode(qrEntity: QrEntity) = qrDao.insertQrCode(qrEntity)

    suspend fun deleteQrCode(qrEntity: QrEntity) = qrDao.deleteQrCode(qrEntity)
}
