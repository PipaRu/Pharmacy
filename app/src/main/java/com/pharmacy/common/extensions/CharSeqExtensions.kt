package com.pharmacy.common.extensions

fun CharSequence.filterNumbers(): CharSequence {
    return filter { it.isDigit() }
}

fun CharSequence.trim(maxLength: Int): CharSequence {
    return if (length > maxLength) {
        substring(0..maxLength)
    } else {
        this
    }
}