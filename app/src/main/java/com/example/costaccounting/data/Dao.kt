package com.example.costaccounting.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions_table WHERE isAnExpense = 0 ORDER BY date DESC, id")
    fun readAllIncomes(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions_table WHERE isAnExpense = 1 ORDER BY date DESC, id")
    fun readAllExpenses(): LiveData<List<Transaction>>

}