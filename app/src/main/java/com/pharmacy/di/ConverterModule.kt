package com.pharmacy.di

import com.pharmacy.common.converter.json.JsonConverter
import com.pharmacy.common.converter.json.JsonConverterImpl
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val convertersModule = module {

    single<Json> {
        Json {
            this.coerceInputValues = true
            this.ignoreUnknownKeys = true
        }
    }

    single<JsonConverter> {
        JsonConverterImpl(
            json = get()
        )
    }

}