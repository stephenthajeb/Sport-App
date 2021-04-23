package com.example.sportapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SportApp: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { SportAppDatabase.getDatabase(this,applicationScope) }
    val scheduleDAO by lazy {database.scheduleDAO()}
}