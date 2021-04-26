package com.example.sportapp.Data

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sportapp.Data.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDAO{
    @Query("SELECT * FROM history_table ORDER BY mode DESC,datetime(date) DESC,time(startTime) DESC")
    fun getAllHistories(): Flow<List<History>>

    @Query ("SELECT * FROM history_table WHERE strftime('%Y',date) IN(:year) AND strftime('%m',date) IN(:month) AND strftime('%d',date) IN(:date)")
    fun getAllHistoriesOnDate(date:String,month:String,year:String): Flow<List<History>>

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History):Long
}
