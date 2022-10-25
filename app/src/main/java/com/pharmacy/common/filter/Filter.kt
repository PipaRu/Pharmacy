package com.pharmacy.common.filter

fun interface Filter<in T> {

    fun filter(value: T): Boolean
}


