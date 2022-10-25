package com.pharmacy.data.repository.product

import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun fetchProducts(
        position: Int,
        count: Int,
        filter: ProductFilter = ProductFilter.Empty,
    ): Flow<List<Product>>

}