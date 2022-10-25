package com.pharmacy.common.converter.model

interface ModelConverter<A, B> {

    fun convert(value: A): B
}