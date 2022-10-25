package com.pharmacy.data.model

import com.pharmacy.common.extensions.emptyString

data class ProductFilter(
    val query: String = emptyString(),
) {
    companion object {
        val Empty: ProductFilter = ProductFilter()
    }
}