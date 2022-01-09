package com.example.costaccounting.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transactions_table",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("account_id"),
    ), ForeignKey(
        entity = USDExchangeRate::class,
        parentColumns = arrayOf("currency_name"),
        childColumns = arrayOf("currency")
    )]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="transaction_id")
    val id: Int,
    val isAnExpense: Boolean,
    @ColumnInfo(name="transaction_amount")
    val amount: Double,
    val account_id: Int,
    val currency: String,
    val category: String,
    val date: Date
)