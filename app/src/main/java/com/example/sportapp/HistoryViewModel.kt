package com.example.sportapp

import androidx.lifecycle.*
import com.example.sportapp.Data.History
import com.example.sportapp.Data.HistoryDAO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HistoryViewModel(private val historyDAO: HistoryDAO): ViewModel() {

    fun insert(history: History) = viewModelScope.launch {
        val result = historyDAO.insert(history)
    }

    //fun getHistoriesOnDate(date:String,month:String,year:String):LiveData<List<History>> = historyDAO.getAllHistoriesOnDate(date,month,year).asLiveData()
    fun getHistoriesOnDate(date:String):LiveData<List<History>> = historyDAO.getAllHistoriesOnDate(date).asLiveData()

     fun getLastHistory(): History = historyDAO.getLastHistoryRecord()
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

