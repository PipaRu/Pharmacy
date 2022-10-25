package com.pharmacy.common.converter.json

import kotlin.reflect.typeOf

inline fun <reified T> JsonConverter.serialize(value: T): String {
    return serialize(value, typeOf<T>())
}

inline fun <reified T> JsonConverter.deserialize(value: String): T {
    return deserialize(value, typeOf<T>())
}

