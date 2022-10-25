package com.pharmacy.data.source.remote.product

import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {

    fun fetchProducts(position: Int, count: Int, filter: ProductFilter): Flow<List<Product>>

    fun updateProducts(products: Set<Product>): Flow<Unit>

    fun deleteProducts(products: Set<Product>): Flow<Unit>

}