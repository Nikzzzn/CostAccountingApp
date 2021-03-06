package com.example.costaccounting.helpers

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.costaccounting.data.DataViewModel
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    val API_KEY = "0ee82fb0-70a5-11ec-9a34-c3c7b67bf578"
    val PREFS_NAME = "preferences"
    val PREF_VERSION_CODE_KEY = "version_code"
    val PREF_BASE_CURRENCY_KEY = "base_currency"
    val VERSION_DOESNT_EXIST = -1
    val CURRENCY_DOESNT_EXIST = "NULL"

    fun getFullDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    }

    fun getDayDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("dd", Locale.getDefault())
    }

    fun getMonthDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("MMMM", Locale.getDefault())
    }

    fun getYearDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("yyyy", Locale.getDefault())
    }

    fun convertCurrency(viewModelStoreOwner: ViewModelStoreOwner, from: String, to: String, amount: Double): Double {
        val dataViewModel = ViewModelProvider(viewModelStoreOwner)[DataViewModel::class.java]
        return amount / dataViewModel.getUSDExchangeRateByName(from) * dataViewModel.getUSDExchangeRateByName(to)
    }

}