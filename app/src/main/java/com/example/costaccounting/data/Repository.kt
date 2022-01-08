package com.example.costaccounting.data

import androidx.lifecycle.LiveData

class Repository(private val dao: Dao) {

    val readAllExpenses: LiveData<List<TransactionWithAccount>> = dao.readAllExpenses()
    val readAllIncomes: LiveData<List<TransactionWithAccount>> = dao.readAllIncomes()
    val readAllAccounts: LiveData<List<Account>> = dao.readAllAccounts()
    val readAllAccountsWithTransactions: LiveData<List<AccountWithTransactions>> = dao.readAllAccountsWithTransactions()

    suspend fun addTransaction(transaction: Transaction){
        dao.addTransaction(transaction)
    }

    suspend fun addAccount(account: Account){
        dao.addAccount(account)
    }

}