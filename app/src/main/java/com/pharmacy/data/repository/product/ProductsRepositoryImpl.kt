package com.pharmacy.data.repository.product

import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.source.remote.product.ProductsRemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductsRepositoryImpl(
    private val remoteDataSource: ProductsRemoteDataSource,
) : ProductsRepository {

    override fun fetchProducts(
        position: Int,
        count: Int,
        filter: ProductFilter,
    ): Flow<List<Product>> {
        return remoteDataSource.fetchProducts(position = position, count = count, filter = filter)
    }

}