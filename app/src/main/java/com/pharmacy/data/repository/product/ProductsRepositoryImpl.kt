package com.pharmacy.data.repository.product

import com.pharmacy.data.model.Category
import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.source.remote.product.ProductsRemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductsRepositoryImpl(
    private val remoteDataSource: ProductsRemoteDataSource,
) : ProductsRepository {

    override val allProducts: Flow<List<Product>>
        get() = remoteDataSource.allProducts

    override fun fetchProducts(
        position: Int,
        count: Int,
        filter: ProductFilter,
    ): Flow<List<Product>> {
        return remoteDataSource.fetchProducts(position = position, count = count, filter = filter)
    }

    override fun getProductCategories(): Flow<List<Category>> = remoteDataSource.getProductCategories()

    override fun getProductCount(filter: ProductFilter): Flow<Int> = remoteDataSource.getProductCount(filter)

    override fun getProductPriceRange(): Flow<IntRange> = remoteDataSource.getProductPriceRange()

}