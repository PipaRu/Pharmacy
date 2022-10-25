package com.pharmacy.common.formatter

interface ParameterizedFormatter<Input, Output, Params> {

    fun format(value: Input, params: Params): Output
}