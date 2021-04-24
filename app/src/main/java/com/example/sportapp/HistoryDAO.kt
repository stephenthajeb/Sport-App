package com.example.sportapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDAO {

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Delete
    suspend fun deleteHistory(history: History)

    @Query("SELECT * FROM history_table WHERE date = :choiceDate")
    fun getAllHistoryInSameDate(choiceDate: String): LiveData<List<History>>
}