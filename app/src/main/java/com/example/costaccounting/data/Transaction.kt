package com.example.costaccounting.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val isAnExpense: Boolean,
    val amount: Double,
    val category: String,
    val date: Date
)