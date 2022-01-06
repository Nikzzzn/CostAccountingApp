package com.example.costaccounting.data

import androidx.lifecycle.LiveData

class Repository(private val dao: Dao) {

    val readAllTransactions: LiveData<List<Transaction>> = dao.readAllTransactions()

    suspend fun addTransaction(transaction: Transaction){
        dao.addTransaction(transaction)
    }

}