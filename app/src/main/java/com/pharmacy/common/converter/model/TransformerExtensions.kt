package com.pharmacy.common.converter.model

fun <O, T> ModelTransformer<O, T>.from(original: O): T {
    return original.transform()
}