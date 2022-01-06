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

    @Query("SELECT * FROM transactions_table ORDER BY id ASC")
    fun readAllTransactions(): LiveData<List<Transaction>>

}