package com.pharmacy.common.formatter

import java.text.NumberFormat
import java.util.*

object CurrencyFormatter : Formatter<Double, String> {

    override fun format(value: Double): String {
        val locale = Locale.getDefault()
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(value)
    }
}