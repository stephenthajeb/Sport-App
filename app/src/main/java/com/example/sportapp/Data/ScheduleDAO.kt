package com.example.sportapp.Data

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sportapp.Data.Schedule
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
