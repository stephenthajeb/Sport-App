package com.example.sportapp.Data

import androidx.annotation.WorkerThread
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO{
    @Query("SELECT * FROM schedule_table")
    fun getAllSchedules(): Flow<List<Schedule>>

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: Schedule):Long

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Update
    suspend fun updateSchedule(schedule: Schedule)
    //Todo: delete, getUserById, updateScheduleTargetAchieved() (?)

}
