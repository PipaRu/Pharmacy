package com.pharmacy.common.converter.model

fun <T, R> T.convert(): R where T : ModelConverter<T, R> = convert(this)

fun <T, R> ModelConverter<T, R>.from(value: T): R = convert(value)