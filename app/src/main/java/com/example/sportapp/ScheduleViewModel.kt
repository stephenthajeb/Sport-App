package com.example.sportapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.sportapp.Data.Schedule
import com.example.sportapp.Data.ScheduleDAO

class ScheduleViewModel(private val scheduleDAO: ScheduleDAO): ViewModel() {
    val schedules : LiveData<List<Schedule>> = scheduleDAO.getAllSchedules().asLiveData()
    suspend fun insert(schedule: Schedule) : Long = scheduleDAO.insert(schedule)

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

