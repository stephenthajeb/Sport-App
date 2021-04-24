package com.example.sportapp

import androidx.lifecycle.*
import com.example.sportapp.Data.Schedule
import com.example.sportapp.Data.ScheduleDAO
import kotlinx.coroutines.launch

class ScheduleViewModel(private val scheduleDAO: ScheduleDAO): ViewModel() {
    val schedules : LiveData<List<Schedule>> = scheduleDAO.getAllSchedules().asLiveData()

    fun insert(schedule: Schedule) = viewModelScope.launch {
        val result = scheduleDAO.insert(schedule)
    }
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

