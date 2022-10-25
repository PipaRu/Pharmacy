package com.pharmacy.common.formatter

object QuantityFormatter : Formatter<Int, String> {

    private const val THINGS = "шт."

    override fun format(value: Int): String {
        return "$value $THINGS"
    }

}