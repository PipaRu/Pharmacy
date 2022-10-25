package com.pharmacy.data.model

import java.util.*

data class Order(
    val id: Long,
    val date: Date,
    val status: Status,
) {
    enum class Status {
        UNDEFINED,
        CREATED,
        PROGRESS,
        COMPLETED,
        CANCELLED,
        FAILED
    }
}