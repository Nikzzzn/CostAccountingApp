package com.example.costaccounting

import java.text.SimpleDateFormat
import java.util.*

object Util {

    private val locale = Locale.forLanguageTag("pl-PL")

    fun getFullDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("dd/MM/yy", locale)
    }

    fun getDayDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("dd", locale)
    }

    fun getMonthDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("LLLL", locale)
    }

    fun getYearDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("yyyy", locale)
    }

}