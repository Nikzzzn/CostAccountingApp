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
    val getAllCategories: LiveData<List<Category>>
    val getExpenseCategories: LiveData<List<Category>>
    val getIncomeCategories: LiveData<List<Category>>
    private val repository: Repository

    init{
        val dao = AppDatabase.getDatabase(application).dao()
        repository = Repository(dao)
        getAllExpenses = repository.getAllExpenses
        getAllIncomes = repository.getAllIncomes
        getAllAccounts = repository.getAllAccounts
        getAllAccountWithTransactions = repository.getAllAccountsWithTransactions
        getAllCurrencies = repository.getAllCurrencies
        getAllCategories = repository.getAllCategories
        getExpenseCategories = repository.getExpenseCategories
        getIncomeCategories = repository.getIncomeCategories
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTransaction(transaction)
        }
    }

    fun addAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            repository.addAccount(account)
        }
    }

    fun updateAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateAccount(account)
        }
    }

    fun deleteAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAccount(account)
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

    fun getTotalSumForAccountById(baseCurrency: String, id: Int): LiveData<Double>{
        return repository.getTotalSumForAccountById(baseCurrency, id)
    }

    fun getUSDExchangeRateByName(name: String): Double{
        return repository.getUSDExchangeRateByName(name)
    }

    fun getAccountById(id: Int): LiveData<Account>{
        return repository.getAccountById(id)
    }

    fun getAllIncomesByAccountId(id: Int): LiveData<List<TransactionWithAccount>>{
        return repository.getAllIncomesByAccountId(id)
    }

    fun getAllExpensesByAccountId(id: Int): LiveData<List<TransactionWithAccount>>{
        return repository.getAllExpensesByAccountId(id)
    }

    fun addCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO){
            repository.addCategory(category)
        }
    }

    fun updateCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteCategory(category)
        }
    }

    fun getCategoryById(id: Int): LiveData<Category>{
        return repository.getCategoryById(id)
    }

}