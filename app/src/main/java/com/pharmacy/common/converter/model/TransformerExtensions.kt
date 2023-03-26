package com.pharmacy.common.converter.model

fun <O, T> ModelTransformer<O, T>.from(original: O): T {
    return original.transform()
}

fun <O, T> ModelTransformer<O, T>.fromAll(original: Iterable<O>): List<T> {
    return original.map { it.transform() }
}