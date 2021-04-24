package com.example.sportapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class SportApp: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { SportAppDatabase.getDatabase(this,applicationScope) }
    val scheduleDAO by lazy {database.scheduleDAO()}
    val historyDAO by lazy {database.historyDAO()}
}