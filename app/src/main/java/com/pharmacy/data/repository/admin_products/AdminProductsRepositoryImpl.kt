package com.pharmacy.data.repository.admin_products

import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.source.remote.product.ProductsRemoteDataSource
import kotlinx.coroutines.flow.Flow

class AdminProductsRepositoryImpl(
    private val remoteDataSource: ProductsRemoteDataSource,
) : AdminProductsRepository {

    override fun getAllProducts(filter: ProductFilter): Flow<List<Product>> {
        return remoteDataSource.fetchProducts(position = 0, count = 100, filter = filter)
    }

    override fun updateProducts(products: Set<Product>): Flow<Unit> {
        return remoteDataSource.updateProducts(products)
    }

    override fun deleteProducts(products: Set<Product>): Flow<Unit> {
        return remoteDataSource.deleteProducts(products)
    }

}