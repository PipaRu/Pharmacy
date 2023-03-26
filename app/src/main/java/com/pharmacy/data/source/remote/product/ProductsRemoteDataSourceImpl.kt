package com.pharmacy.data.source.remote.product

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.pharmacy.common.converter.json.JsonConverter
import com.pharmacy.common.converter.json.deserialize
import com.pharmacy.common.converter.model.convertAll
import com.pharmacy.common.converter.model.fromAll
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.common.extensions.flowOf
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.Category
import com.pharmacy.data.model.Price
import com.pharmacy.data.model.Product
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.source.remote.model.ProductDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.io.InputStream
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.reflect.typeOf

class ProductsRemoteDataSourceImpl(
    jsonConverter: JsonConverter,
    application: Application,
) : ProductsRemoteDataSource {

    companion object {
        private const val SP_MOCK_PRODUCTS = "mock_products"
        private const val KEY_MOCK_PRODUCTS = "products"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val observableProducts: MutableStateFlow<List<Product>>

    private val sharedPreferences by lazy {
        application.getSharedPreferences(SP_MOCK_PRODUCTS, Context.MODE_PRIVATE)
    }

    override val allProducts: Flow<List<Product>>
        get() = observableProducts

    init {
        val productsJson = sharedPreferences.getString(KEY_MOCK_PRODUCTS, null)
        val products: List<Product> = if (productsJson.isNullOrEmpty()) {
            MockedProductFactory.getProductsFromJson(
                application = application,
                jsonConverter = jsonConverter
            )
        } else {
            jsonConverter.deserialize<List<ProductDto>>(productsJson).convertAll()
        }

        observableProducts = MutableStateFlow(products)

        observableProducts
            .onEach { newProducts ->
                sharedPreferences.edit(commit = true) {
                    putString(KEY_MOCK_PRODUCTS, jsonConverter.serialize(ProductDto.fromAll(newProducts), typeOf<List<ProductDto>>()))
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(scope)

    }

    override fun createProduct(): Flow<Product> = flowOf {
        var id: Int
        do {
            id = Random.nextInt()
        } while (observableProducts.value.find { it.id == id } != null)
        Product(
            id = id,
            name = emptyString(),
            imageUrl = emptyString(),
            description = emptyString(),
            categories = emptyList(),
            price = Price.Empty,
        )
    }

    override fun fetchProducts(
        position: Int,
        count: Int,
        filter: ProductFilter,
    ): Flow<List<Product>> = getProducts(filter).map { products ->
        if (products.isEmpty()) emptyList()
        else products.subList(position, (position + count).coerceAtMost(products.size))
    }

    override fun updateProducts(products: Set<Product>): Flow<Unit> {
        return flowOf {
            val newProducts: MutableList<Product> = observableProducts.value.toMutableList()
            products.forEach { product ->
                val index = newProducts.indexOfFirst { product.id == it.id }
                if (index >= 0) {
                    newProducts[index] = product
                } else {
                    newProducts.add(product)
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


    override fun getProductCategories(): Flow<List<Category>> = flowOf {
        observableProducts.value.map { product ->
            product.categories
        }.flatten().distinct()
    }

    override fun getProductCount(filter: ProductFilter): Flow<Int> {
        return getProducts(filter).map(List<Product>::count)
    }

    override fun getProductPriceRange(): Flow<IntRange> = observableProducts.map { products ->
        val min = products.minOf { product -> product.price.finalPrice }.roundToInt()
        val max = products.maxOf { product -> product.price.finalPrice }.roundToInt()
        IntRange(min, max)
    }

    private fun getProducts(filter: ProductFilter): Flow<List<Product>> {
        return observableProducts.map { products ->
            products.filter { product ->
                Filtering.filterProduct(product, filter)
            }
                .reversed()
        }
            .take(1)
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
                    .convertAll()
            } catch (e: Throwable) {
                Crashlytics.record(e)
                emptyList()
            }
        }

    }

    object Filtering {

        fun filterProduct(product: Product, productFilter: ProductFilter): Boolean {
            return filterByQuery(product, productFilter.query)
                    && filterByPriceRange(product, productFilter.priceRange)
                    && filterByCategory(product, productFilter.categories)
        }

        private fun filterByQuery(product: Product, query: String?): Boolean {
            if (query == null) return true
            val id: Int? = query.toIntOrNull()
            return if (id != null) {
                product.id.toString().contains(id.toString())
            } else {
                val isNameContains = product.name.contains(other = query, ignoreCase = true)
                val isDescriptionContains =
                    product.description.contains(other = query, ignoreCase = true)
                isNameContains || isDescriptionContains
            }
        }


        private fun filterByPriceRange(product: Product, priceRange: IntRange?): Boolean {
            if (priceRange == null) return true
            return product.price.finalPrice.toInt() in (priceRange.first..priceRange.last)
        }

        private fun filterByCategory(product: Product, categories: List<Category>?): Boolean {
            if (categories == null) return true
            return categories.any { selectedCategory ->
                product.categories.any { productCategory ->
                    productCategory.name == selectedCategory.name
                }
            }
        }

    }

}