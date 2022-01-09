package com.example.costaccounting.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usd_exchange_rates")
data class USDExchangeRate(
    @PrimaryKey
    val currency_name: String,
    val currency_value: Double
)