package com.pharmacy.common.formatter

interface Formatter<Input, Output> {

    fun format(value: Input): Output
}