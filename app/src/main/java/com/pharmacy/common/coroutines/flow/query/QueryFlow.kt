package com.pharmacy.common.coroutines.flow.query

import kotlinx.coroutines.flow.StateFlow

interface QueryFlow : StateFlow<String> {

    val pending: StateFlow<String>

    override val value: String

    suspend fun query(value: String)

    suspend fun execute(query: String = pending.value)
}