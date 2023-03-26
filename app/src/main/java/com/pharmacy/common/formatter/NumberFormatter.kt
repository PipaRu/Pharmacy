package com.pharmacy.common.formatter

import java.text.NumberFormat
import java.util.*

object NumberFormatter : Formatter<Number, String> {

    override fun format(value: Number): String {
        val locale = Locale.getDefault()
        val formatter = NumberFormat.getNumberInstance(locale)
        return formatter.format(value)
    }

}