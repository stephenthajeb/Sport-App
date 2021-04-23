package com.example.sportapp

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScheduleViewModel(private val scheduleDAO: ScheduleDAO): ViewModel() {
    val schedules : LiveData<List<Schedule>> = scheduleDAO.getAllSchedules().asLiveData()

    fun insert(schedule:Schedule) = viewModelScope.launch {
        scheduleDAO.insert(schedule)
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

