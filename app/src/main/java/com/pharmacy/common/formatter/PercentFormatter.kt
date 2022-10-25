package com.pharmacy.common.formatter

import java.text.DecimalFormat

object PercentFormatter : Formatter<Double, String> {

    private const val PERCENT = '%'

    private val percentFormatter = DecimalFormat("###,###.##")

    override fun format(value: Double): String {
        return percentFormatter.format(value) + " $PERCENT"
    }

}