package com.example.costaccounting.data

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionWithAccount(
    @Embedded
    val transaction: Transaction,
    val accountName: String
): Parcelable