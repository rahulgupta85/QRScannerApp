package com.example.appdev.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [QrEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QrDatabase : RoomDatabase() {
    abstract fun qrDao(): QrDao
}
