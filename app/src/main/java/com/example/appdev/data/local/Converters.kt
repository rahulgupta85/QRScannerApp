package com.example.appdev.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromQrType(value: QrType): String {
        return value.name
    }

    @TypeConverter
    fun toQrType(value: String): QrType {
        return QrType.valueOf(value)
    }
}
