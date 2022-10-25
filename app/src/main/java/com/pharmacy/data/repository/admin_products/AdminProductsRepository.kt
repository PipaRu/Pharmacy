package com.pharmacy.data.repository.admin_products

import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import kotlinx.coroutines.flow.Flow

interface AdminProductsRepository {

    fun getAllProducts(filter: ProductFilter = ProductFilter.Empty): Flow<List<Product>>

    fun updateProducts(products: Set<Product>): Flow<Unit>

    fun deleteProducts(products: Set<Product>): Flow<Unit>

}