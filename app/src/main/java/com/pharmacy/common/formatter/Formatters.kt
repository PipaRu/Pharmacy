package com.pharmacy.common.formatter

import com.pharmacy.common.model.PhoneNumber
import java.util.*

object Formatters {

    val currency: Formatter<Double, String> = CurrencyFormatter

    val date: Formatter<Date, String> = DateFormatter

    val percent: Formatter<Double, String> = PercentFormatter

    val phone: Formatter<PhoneNumber, String> = PhoneFormatter

    val quantity: Formatter<Int, String> = QuantityFormatter

}