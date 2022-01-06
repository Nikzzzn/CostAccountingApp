package com.example.costaccounting.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel(application: Application): AndroidViewModel(application) {

    private val readAllTransactions: LiveData<List<Transaction>>
    private val repository: Repository

    init{
        val dao = AppDatabase.getDatabase(application).dao()
        repository = Repository(dao)
        readAllTransactions = repository.readAllTransactions
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransaction(transaction)
        }
    }

}