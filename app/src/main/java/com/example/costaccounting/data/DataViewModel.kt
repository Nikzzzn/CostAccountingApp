package com.example.costaccounting.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DataViewModel(application: Application): AndroidViewModel(application) {

    val getAllExpenses: LiveData<List<TransactionWithAccount>>
    val getAllIncomes: LiveData<List<TransactionWithAccount>>
    val getAllAccounts: LiveData<List<Account>>
    val getAllAccountWithTransactions: LiveData<List<AccountWithTransactions>>
    val getAllCurrencies: LiveData<List<String>>
    private val repository: Repository

    init{
        val dao = AppDatabase.getDatabase(application).dao()
        repository = Repository(dao)
        getAllExpenses = repository.getAllExpenses
        getAllIncomes = repository.getAllIncomes
        getAllAccounts = repository.getAllAccounts
        getAllAccountWithTransactions = repository.getAllAccountsWithTransactions
        getAllCurrencies = repository.getAllCurrencies
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

    fun addUSDExchangeRate(usdExchangeRate: USDExchangeRate){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUSDExchangeRate(usdExchangeRate)
        }
    }

    fun getTotalSumForAllAccounts(baseCurrency: String): LiveData<Double>{
        return repository.getTotalSumForAllAccounts(baseCurrency)
    }

    fun getUSDExchangeRateByName(name: String): Double{
        return repository.getUSDExchangeRateByName(name)
    }

}