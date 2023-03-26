package com.pharmacy.data.source.remote.product

import com.pharmacy.data.model.Category
import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {

    val allProducts: Flow<List<Product>>

    fun createProduct(): Flow<Product>

    fun fetchProducts(position: Int, count: Int, filter: ProductFilter): Flow<List<Product>>

    fun updateProducts(products: Set<Product>): Flow<Unit>

    fun deleteProducts(products: Set<Product>): Flow<Unit>

    fun getProductCategories(): Flow<List<Category>>

    fun getProductCount(filter: ProductFilter): Flow<Int>

    fun getProductPriceRange(): Flow<IntRange>

}