package com.pharmacy.common.formatter

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter : Formatter<Date, String> {

    private val locale = Locale.getDefault()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", locale)

    override fun format(value: Date): String {
        return dateFormat.format(value)
    }

}