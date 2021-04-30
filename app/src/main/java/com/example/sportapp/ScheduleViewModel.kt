package com.example.sportapp

import androidx.lifecycle.*
import com.example.sportapp.Data.Schedule
import com.example.sportapp.Data.ScheduleDAO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ScheduleViewModel(private val scheduleDAO: ScheduleDAO): ViewModel() {
    val schedules : LiveData<List<Schedule>> = scheduleDAO.getAllSchedules().asLiveData()
    suspend fun insert(schedule: Schedule) : Long = scheduleDAO.insert(schedule)

    suspend fun delete(schedule:Schedule) =  scheduleDAO.deleteSchedule(schedule)

}

class ScheduleModelFactory(private val scheduleDAO: ScheduleDAO):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(scheduleDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

