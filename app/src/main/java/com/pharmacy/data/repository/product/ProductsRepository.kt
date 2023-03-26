package com.pharmacy.data.repository.product

import com.pharmacy.data.model.Category
import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    val allProducts: Flow<List<Product>>

    fun fetchProducts(
        position: Int,
        count: Int,
        filter: ProductFilter = ProductFilter.Empty,
    ): Flow<List<Product>>

    fun getProductCategories(): Flow<List<Category>>

    fun getProductCount(filter: ProductFilter): Flow<Int>

    fun getProductPriceRange(): Flow<IntRange>

}