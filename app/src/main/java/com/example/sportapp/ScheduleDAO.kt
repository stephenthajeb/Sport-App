package com.example.sportapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO{
    @Query("SELECT * FROM schedule_table")
    fun getAllSchedules(): Flow<List<Schedule>>

    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: Schedule) : Int //return the number affected by operation

    //Todo: delete, getUserById, updateScheduleTargetAchieved() (?)
}
