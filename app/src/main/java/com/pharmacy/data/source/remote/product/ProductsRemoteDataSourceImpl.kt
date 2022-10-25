package com.pharmacy.data.source.remote.product

import android.app.Application
import com.pharmacy.common.converter.json.JsonConverter
import com.pharmacy.common.converter.json.deserialize
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.extensions.flowOf
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.source.remote.model.ProductDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream

class ProductsRemoteDataSourceImpl(
    jsonConverter: JsonConverter,
    application: Application,
) : ProductsRemoteDataSource {

    private val observableProducts = MutableStateFlow(
        MockedProductFactory.getProductsFromJson(
            application = application,
            jsonConverter = jsonConverter
        )
    )

    override fun fetchProducts(
        position: Int,
        count: Int,
        filter: ProductFilter,
    ): Flow<List<Product>> {
        return observableProducts.map { products ->
            products
                .filter { product ->
                    val queryId: Int? = filter.query.toIntOrNull()
                    if (queryId != null) {
                        product.id == queryId
                    } else {
                        product.name.contains(other = filter.query, ignoreCase = true)
                                || product.description.contains(
                            other = filter.query,
                            ignoreCase = true
                        )
                    }
                }.let {
                    if (it.isEmpty()) emptyList()
                    else it.subList(position, (position + count).coerceAtMost(it.size))
                }
        }
    }

    override fun updateProducts(products: Set<Product>): Flow<Unit> {
        return flowOf {
            val newProducts: MutableList<Product> = observableProducts.value.toMutableList()
            products.forEach { product ->
                val index = newProducts.indexOfFirst { product.id == it.id }
                if (index >= 0) {
                    newProducts[index] = product
                }
            }
            observableProducts.emit(newProducts)
        }
    }

    override fun deleteProducts(products: Set<Product>): Flow<Unit> {
        return flowOf {
            val newProducts: MutableList<Product> = observableProducts.value.toMutableList()
            newProducts.removeIf { removeCandidate ->
                products.any { product -> product.id == removeCandidate.id }
            }
            observableProducts.emit(newProducts)
        }
    }


    private object MockedProductFactory {

        private fun loadFromFile(application: Application): String? {
            var json: String? = null
            json = try {
                val `is`: InputStream = application.assets.open("products.json")
                val size: Int = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }

        fun getProductsFromJson(
            application: Application,
            jsonConverter: JsonConverter,
        ): List<Product> {
            return try {
                jsonConverter.deserialize<List<ProductDto>>(loadFromFile(application).orEmpty())
                    .map {
                        it.convert()
                    }
            } catch (e: Throwable) {
                Crashlytics.record(e)
                emptyList()
            }
        }

    }

}