package com.pharmacy.common.formatter

import com.pharmacy.common.extensions.Empty
import com.pharmacy.common.model.PhoneNumber

object PhoneFormatter : Formatter<PhoneNumber, String> {

    const val MASK_NUMBER_INDICATOR = 'x'
    const val MASK = "+7 (xxx) xxx-xx-xx"
    const val MAX_NUMBERS_WITH_COUNTRY_CODE = 11
    const val MAX_NUMBERS = 10

    override fun format(value: PhoneNumber): String {
        var index = 0
        return MASK.map<Char> { char ->
            if (char == MASK_NUMBER_INDICATOR) {
                value.number.getOrElse(index) { MASK_NUMBER_INDICATOR }.also {
                    index++
                }
            } else {
                char
            }
        }.joinToString(separator = String.Empty)
    }

}