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

    suspend fun addAccount(account: Account){
        dao.addAccount(account)
    }

    suspend fun updateAccount(account: Account){
        dao.updateAccount(account)
    }

    suspend fun addUSDExchangeRate(usdExchangeRate: USDExchangeRate){
        dao.addUSDExchangeRate(usdExchangeRate)
    }

    fun getTotalSumForAllAccounts(baseCurrency: String): LiveData<Double>{
        return dao.getTotalSumForAllAccounts(baseCurrency)
    }

    fun getUSDExchangeRateByName(name: String): Double{
        return dao.getUSDExchangeRateByName(name)
    }

}