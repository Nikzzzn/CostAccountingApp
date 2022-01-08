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

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName " +
            "FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "WHERE isAnExpense = 0 ORDER BY date DESC, transaction_id DESC")
    fun readAllIncomes(): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName " +
            "FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "WHERE isAnExpense = 1 ORDER BY date DESC, transaction_id DESC")
    fun readAllExpenses(): LiveData<List<TransactionWithAccount>>

    /*@Query("SELECT * FROM transactions_table WHERE isAnExpense = 1 ORDER BY date DESC, transaction_id DESC")
    fun readAllExpenses(): LiveData<List<Transaction>>*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: Account)

    @Query("SELECT * FROM accounts_table ORDER BY id ASC")
    fun readAllAccounts(): LiveData<List<Account>>

    @androidx.room.Transaction
    @Query("SELECT * FROM accounts_table ORDER BY id ASC")
    fun readAllAccountsWithTransactions(): LiveData<List<AccountWithTransactions>>

}