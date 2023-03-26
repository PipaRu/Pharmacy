package com.pharmacy.common.converter.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KType

class JsonConverterImpl(
    private val json: Json
) : JsonConverter {

    override fun <T> serialize(value: T, type: KType): String {
        val serializer: KSerializer<T> = json.serializersModule.serializer(type).cast()
        return json.encodeToString(serializer, value)
    }

    override fun <T> deserialize(value: String, type: KType): T {
        val serializer: KSerializer<T> = json.serializersModule.serializer(type).cast()
        return json.decodeFromString(serializer, value)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T> KSerializer<*>.cast(): KSerializer<T> = this as KSerializer<T>

}