package com.example.costaccounting.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(
    tableName = "accounts_table",
    foreignKeys = [ForeignKey(
        entity = USDExchangeRate::class,
        parentColumns = arrayOf("currency_name"),
        childColumns = arrayOf("currency")
    )]
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val amount: Double,
    val currency: String
): Parcelable