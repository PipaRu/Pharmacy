package com.pharmacy.common.extensions

private const val EMPTY_STRING = ""

fun emptyString(): String = EMPTY_STRING

val String.Companion.Empty: String
    get() = EMPTY_STRING

fun String.filterNumbers(): String {
    return filter { it.isDigit() }
}

fun String.trim(maxLength: Int): String {
    return if (length > maxLength) {
        substring(0..maxLength)
    } else {
        this
    }
}