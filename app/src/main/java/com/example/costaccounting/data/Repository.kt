package com.example.costaccounting.data

import androidx.lifecycle.LiveData

class Repository(private val dao: Dao) {

    val getAllExpenses: LiveData<List<TransactionWithAccount>> = dao.getAllExpenses()
    val getAllIncomes: LiveData<List<TransactionWithAccount>> = dao.getAllIncomes()
    val getAllAccounts: LiveData<List<Account>> = dao.getAllAccounts()
    val getAllAccountsWithTransactions: LiveData<List<AccountWithTransactions>> = dao.getAllAccountsWithTransactions()
    val getAllCurrencies: LiveData<List<String>> = dao.getAllCurrencies()

    suspend fun addTransaction(transaction: Transaction){
        dao.addTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction){
        dao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction){
        dao.deleteTransaction(transaction)
    }

    fun getAllIncomesByAccountId(id: Int): LiveData<List<TransactionWithAccount>>{
        return dao.getAllIncomesByAccountId(id)
    }

    fun getAllExpensesByAccountId(id: Int): LiveData<List<TransactionWithAccount>>{
        return dao.getAllExpensesByAccountId(id)
    }

    fun getExpensesByDay(dayOfYear: String): LiveData<List<TransactionWithAccount>>{
        return dao.getExpensesByDay(dayOfYear)
    }

    suspend fun addAccount(account: Account){
        dao.addAccount(account)
    }

    suspend fun updateAccount(account: Account){
        dao.updateAccount(account)
    }
    suspend fun deleteAccount(account: Account){
        dao.deleteAccount(account)
    }

    suspend fun addUSDExchangeRate(usdExchangeRate: USDExchangeRate){
        dao.addUSDExchangeRate(usdExchangeRate)
    }

    fun getTotalSumForAllAccounts(baseCurrency: String): LiveData<Double>{
        return dao.getTotalSumForAllAccounts(baseCurrency)
    }

    fun getTotalSumForAccountById(baseCurrency: String, id: Int): LiveData<Double>{
        return dao.getTotalSumForAccountById(baseCurrency, id)
    }

    fun getUSDExchangeRateByName(name: String): Double{
        return dao.getUSDExchangeRateByName(name)
    }

    fun getAccountById(id: Int): LiveData<Account>{
        return dao.getAccountById(id)
    }

}