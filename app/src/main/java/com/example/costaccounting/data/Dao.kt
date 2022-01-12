package com.example.costaccounting.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {

    //Transaction

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName, " +
            "categories_table.name AS categoryName FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "INNER JOIN categories_table ON transactions_table.category_id = categories_table.id " +
            "WHERE isAnExpense = 0 ORDER BY date DESC, transaction_id DESC")
    fun getAllIncomes(): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName, " +
            "categories_table.name AS categoryName FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "INNER JOIN categories_table ON transactions_table.category_id = categories_table.id " +
            "WHERE isAnExpense = 0 AND accounts_table.id = :id " +
            "ORDER BY date DESC, transaction_id DESC")
    fun getAllIncomesByAccountId(id: Int): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName, " +
            "categories_table.name AS categoryName FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "INNER JOIN categories_table ON transactions_table.category_id = categories_table.id " +
            "WHERE isAnExpense = 1 ORDER BY date DESC, transaction_id DESC")
    fun getAllExpenses(): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName, " +
            "categories_table.name AS categoryName FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "INNER JOIN categories_table ON transactions_table.category_id = categories_table.id " +
            "WHERE isAnExpense = 1 AND accounts_table.id = :id " +
            "ORDER BY date DESC, transaction_id DESC")
    fun getAllExpensesByAccountId(id: Int): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName, " +
            "categories_table.name AS categoryName FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "INNER JOIN categories_table ON transactions_table.category_id = categories_table.id " +
            "WHERE isAnExpense = 1 AND strftime('%j',date) = :dayOfYear " +
            "ORDER BY date DESC, transaction_id DESC")
    fun getExpensesByDay(dayOfYear: String): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName, " +
            "categories_table.name AS categoryName FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "INNER JOIN categories_table ON transactions_table.category_id = categories_table.id " +
            "WHERE isAnExpense = 1 AND strftime('%W',date) = :weekOfYear " +
            "ORDER BY date DESC, transaction_id DESC")
    fun getExpensesByWeek(weekOfYear: String): LiveData<List<TransactionWithAccount>>

    @Query("SELECT transactions_table.*, accounts_table.name AS accountName, " +
            "categories_table.name AS categoryName FROM transactions_table " +
            "INNER JOIN accounts_table ON transactions_table.account_id = accounts_table.id " +
            "INNER JOIN categories_table ON transactions_table.category_id = categories_table.id " +
            "WHERE isAnExpense = 1 AND strftime('%m',date) = :month " +
            "ORDER BY date DESC, transaction_id DESC")
    fun getExpensesByMonth(month: String): LiveData<List<TransactionWithAccount>>

    //Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT * FROM accounts_table ORDER BY id ASC")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM accounts_table WHERE id = :id")
    fun getAccountById(id: Int): LiveData<Account>

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

    @Query("SELECT sum(amount / currency_value * base_currency_value) " +
            "FROM accounts_table, " +
            "(SELECT currency_value AS base_currency_value " +
            "FROM usd_exchange_rates " +
            "WHERE currency_name = :baseCurrency) " +
            "INNER JOIN usd_exchange_rates ON currency = currency_name " +
            "WHERE id = :id")
    fun getTotalSumForAccountById(baseCurrency: String, id: Int): LiveData<Double>

    //USDExchangeRate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUSDExchangeRate(usdExchangeRate: USDExchangeRate)

    @Query("SELECT currency_value FROM usd_exchange_rates WHERE currency_name = :name")
    fun getUSDExchangeRateByName(name: String): Double

    @Query("SELECT currency_name FROM usd_exchange_rates")
    fun getAllCurrencies(): LiveData<List<String>>

    //Category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories_table")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories_table WHERE isAnExpenseCategory = 1")
    fun getExpenseCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories_table WHERE isAnExpenseCategory = 0")
    fun getIncomeCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories_table WHERE id = :id")
    fun getCategoryById(id: Int): LiveData<Category>

}