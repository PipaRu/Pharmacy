package com.pharmacy.common.extensions

val Double.Companion.Zero: Double
    get() = 0.0

val Float.Companion.Zero: Float
    get() = 0.0f

val Long.Companion.Zero: Long
    get() = 0L

val Int.Companion.Zero: Int
    get() = 0

fun Double?.orZero(): Double = this ?: Double.Zero

fun Float?.orZero(): Float = this ?: Float.Zero

fun Long?.orZero(): Long = this ?: Long.Zero

fun Int?.orZero(): Int = this ?: Int.Zero