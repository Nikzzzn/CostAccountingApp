package com.example.costaccounting.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity(
    tableName = "transactions_table",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("account_id"),
        onDelete = CASCADE
    ), ForeignKey(
        entity = USDExchangeRate::class,
        parentColumns = arrayOf("currency_name"),
        childColumns = arrayOf("currency")
    ), ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = CASCADE
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
    val category_id: Int,
    val date: Date
): Parcelable