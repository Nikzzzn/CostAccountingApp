package com.example.costaccounting.data

import androidx.lifecycle.LiveData

class Repository(private val dao: Dao) {

    val readAllExpenses: LiveData<List<Transaction>> = dao.readAllExpenses()
    val readAllIncomes: LiveData<List<Transaction>> = dao.readAllIncomes()
    val readAllAccounts: LiveData<List<Account>> = dao.readAllAccounts()

    suspend fun addTransaction(transaction: Transaction){
        dao.addTransaction(transaction)
    }

    suspend fun addAccount(account: Account){
        dao.addAccount(account)
    }

}