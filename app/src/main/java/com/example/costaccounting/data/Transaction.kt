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
    val category: String,
    val date: Date
    //добавить привязку к аккаунту и просьбу о создании аккаунта при первом запуске приложения
    )