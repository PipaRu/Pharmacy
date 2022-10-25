package com.pharmacy.data.model

import java.io.Serializable

data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String,
    val category: String,
    val price: Price,
) : Serializable