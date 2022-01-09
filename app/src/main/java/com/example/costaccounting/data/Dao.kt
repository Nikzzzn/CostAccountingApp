package com.example.costaccounting.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    //Transaction

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: Transaction)

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName " +
            "FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "WHERE isAnExpense = 0 ORDER BY date DESC, transaction_id DESC")
    fun getAllIncomes(): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName " +
            "FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "WHERE isAnExpense = 1 ORDER BY date DESC, transaction_id DESC")
    fun getAllExpenses(): LiveData<List<TransactionWithAccount>>

    //Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: Account)

    @Query("SELECT * FROM accounts_table ORDER BY id ASC")
    fun getAllAccounts(): LiveData<List<Account>>

    @androidx.room.Transaction
    @Query("SELECT * FROM accounts_table ORDER BY id ASC")
    fun getAllAccountsWithTransactions(): LiveData<List<AccountWithTransactions>>

    @Query("SELECT sum(amount / currency_value * base_currency_value) " +
            "FROM accounts_table, " +
                "(SELECT currency_value AS base_currency_value " +
                "FROM usd_exchange_rates " +
                "WHERE currency_name = :baseCurrency) " +
            "INNER JOIN usd_exchange_rates ON currency = currency_name")
    fun getTotalSumForAllAccounts(baseCurrency: String): LiveData<Double>

    //USDExchangeRate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUSDExchangeRate(usdExchangeRate: USDExchangeRate)

    @Query("SELECT currency_value FROM usd_exchange_rates WHERE currency_name = :name")
    fun getUSDExchangeRateByName(name: String): Double

    @Query("SELECT currency_name FROM usd_exchange_rates")
    fun getAllCurrencies(): LiveData<List<String>>

}