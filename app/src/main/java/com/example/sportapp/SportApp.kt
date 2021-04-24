package com.example.sportapp

import android.app.Application
import com.example.sportapp.Data.SportAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SportApp: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { SportAppDatabase.getDatabase(this,applicationScope) }
    val scheduleDAO by lazy {database.scheduleDAO()}
    val historyDAO by lazy {database.historyDAO()}
}