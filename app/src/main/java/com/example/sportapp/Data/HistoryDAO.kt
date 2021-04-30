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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Query("SELECT * FROM history_table WHERE id=(SELECT MAX(id) FROM history_table)")
    fun getLastHistoryRecord():History

    @Query ("SELECT * FROM history_table WHERE date LIKE :date")
    fun getAllHistoriesOnDate(date:String): Flow<List<History>>

    @Query("SELECT * FROM history_table WHERE id=:id LIMIT 1")
    fun retriveSingleHistoryById(id: Int): Flow<History>
    

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: History):Long


}
