package com.pharmacy.data.model

import com.pharmacy.common.extensions.Zero
import java.io.Serializable

data class Price(
    val finalPrice: Double,
    val startPrice: Double,
    val discount: Double,
) : Serializable {
    companion object {
        val Empty: Price = Price(
            finalPrice = Double.Zero,
            startPrice = Double.Zero,
            discount = Double.Zero,
        )
    }
}