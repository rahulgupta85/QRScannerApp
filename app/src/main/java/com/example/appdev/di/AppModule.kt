package com.example.appdev.di

import android.content.Context
import androidx.room.Room
import com.example.appdev.data.local.QrDao
import com.example.appdev.data.local.QrDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQrDatabase(@ApplicationContext context: Context): QrDatabase {
        return Room.databaseBuilder(
            context,
            QrDatabase::class.java,
            "qr_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQrDao(database: QrDatabase): QrDao {
        return database.qrDao()
    }
}
