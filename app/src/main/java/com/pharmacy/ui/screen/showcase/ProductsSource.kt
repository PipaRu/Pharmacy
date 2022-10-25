package com.pharmacy.ui.screen.showcase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.extensions.execute
import com.pharmacy.common.extensions.orZero
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class ProductsSource(
    private val repository: ProductsRepository,
    private val productFilter: ProductFilter,
) : PagingSource<Int, ProductItem>() {

    companion object {
        private const val START_KEY = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> = execute {
        val loadSize = params.loadSize
        val currentKey = params.key ?: START_KEY

        val from = currentKey * loadSize

        val items = repository.fetchProducts(
            position = from,
            count = loadSize,
            filter = productFilter
        ).first().map(ProductItem.Companion::from)

        val prevKey = if (currentKey == 0) null else currentKey - 1
        val nextKey = if (items.size < loadSize) null else currentKey.inc()

        val itemsBefore = prevKey?.times(loadSize).orZero()
        val itemsNext = nextKey?.times(loadSize).orZero()

        delay(MockDelay.LONG)

        LoadResult.Page(
            data = items,
            prevKey = prevKey,
            nextKey = nextKey,
            itemsBefore = itemsBefore,
            itemsAfter = itemsNext
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return null
    }

}