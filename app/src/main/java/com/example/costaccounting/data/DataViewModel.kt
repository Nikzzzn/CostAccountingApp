package com.example.costaccounting.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel(application: Application): AndroidViewModel(application) {

    val readAllExpenses: LiveData<List<Transaction>>
    val readAllIncomes: LiveData<List<Transaction>>
    val readAllAccounts: LiveData<List<Account>>
    private val repository: Repository

    init{
        val dao = AppDatabase.getDatabase(application).dao()
        repository = Repository(dao)
        readAllExpenses = repository.readAllExpenses
        readAllIncomes = repository.readAllIncomes
        readAllAccounts = repository.readAllAccounts
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransaction(transaction)
        }
    }

    fun addAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            repository.addAccount(account)
        }
    }

}