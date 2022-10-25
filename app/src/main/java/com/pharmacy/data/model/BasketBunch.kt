package com.pharmacy.data.model

data class BasketBunch(
    val id: Int,
    val product: Product,
    val count: Int,
) {
    constructor(product: Product, count: Int) : this(
        id = product.id,
        product = product,
        count = count
    )
}