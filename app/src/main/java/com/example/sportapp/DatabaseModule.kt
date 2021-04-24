package com.example.sportapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideHistoryDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        SportAppDatabase::class.java,
        "sportapp_db"
    ).build()

    @Singleton
    @Provides
    fun provideHistoryDao(db: SportAppDatabase) = db.historyDAO()
}