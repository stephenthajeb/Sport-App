package com.example.sportapp

import androidx.lifecycle.*
import com.example.sportapp.Data.History
import com.example.sportapp.Data.HistoryDAO
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyDAO: HistoryDAO): ViewModel() {
    val histories : LiveData<List<History>> = historyDAO.getAllHistories().asLiveData()

    fun insert(history: History) = viewModelScope.launch {
        val result = historyDAO.insert(history)
    }
}

class HistoryModelFactory(private val historyDAO: HistoryDAO): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(historyDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}