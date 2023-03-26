package com.pharmacy.data.model

data class ProductFilter(
    val query: String? = null,
    val priceRange: IntRange? = null,
    val categories: List<Category>? = null,
) {
    companion object {
        val Empty: ProductFilter = ProductFilter()
    }
}