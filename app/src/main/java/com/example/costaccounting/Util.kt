package com.example.costaccounting

import java.text.SimpleDateFormat
import java.util.*

object Util {

    fun getDateFormat(): SimpleDateFormat{
        return SimpleDateFormat("dd/MM/yy", Locale.forLanguageTag("pl-PL"))
    }

}