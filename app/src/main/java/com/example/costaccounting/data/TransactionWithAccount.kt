package com.example.costaccounting.data

import androidx.room.Embedded

data class TransactionWithAccount(
    @Embedded
    val transaction: Transaction,
    val accountName: String
)