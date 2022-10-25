package com.pharmacy.common.converter.json

import kotlin.reflect.KType

interface JsonConverter {

    fun <T> serialize(value: T, type: KType): String

    fun <T> deserialize(value: String, type: KType): T
}