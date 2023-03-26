package com.pharmacy.ui.screen.showcase

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.coroutines.flow.query.QueryFlow
import com.pharmacy.common.extensions.Empty
import com.pharmacy.common.extensions.filter
import com.pharmacy.common.extensions.orZero
import com.pharmacy.common.extensions.updateElement
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.common.paging.PagingList
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.repository.basket.BasketRepository
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.screen.showcase.model.ShowcaseItem
import com.pharmacy.ui.screen.showcase.model.mvi.ShowcaseSideEffect
import com.pharmacy.ui.screen.showcase.model.mvi.ShowcaseViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class ShowcaseViewModel(
    private val productsRepository: ProductsRepository,
    private val basketRepository: BasketRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<ShowcaseViewState, ShowcaseSideEffect> {

    companion object {
        private const val QUERY_DEBOUNCE_MILLISECONDS = 1_000L
        private const val PRODUCTS_PAGE_SIZE = 10
    }

    override val container = container<ShowcaseViewState, ShowcaseSideEffect>(
        initialState = ShowcaseViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(ShowcaseSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    private val queryFlow: QueryFlow = QueryFlow(
        debounceMilliseconds = QUERY_DEBOUNCE_MILLISECONDS,
        coroutineScope = viewModelScope,
        initialQuery = String.Empty,
    )

    private val pager = Pager(
        config = PagingConfig(pageSize = PRODUCTS_PAGE_SIZE),
        initialKey = null,
        pagingSourceFactory = {
            ProductsSource(
                repository = productsRepository,
                productFilter = ProductFilter(
                    query = queryFlow.value
                )
            )
        }
    )

    private val pagingList = PagingList<ProductItem>().apply {
        viewModelScope.launch {
            collectPagingData(pager.flow.cachedIn(this))
        }
    }

    init {
        //Если продукты изменилилсь то рефреш
        productsRepository.allProducts
            .onEach { pagingList.refresh() }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)

        //Подписываемся на запрос и при обновление рефрешим список
        queryFlow
            .map(::ProductFilter)
            .onEach { pagingList.refresh() }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)

        //Продукты в корзине
        val basketFlow = basketRepository.getAll()
            .flowOn(Dispatchers.IO)

        //Обрабатываем данные с бэка и карзины
        val productItems: Flow<List<ShowcaseItem>> = combine(
            pagingList.items,
            basketFlow,
            container.stateFlow.map { it.items }
        ) { products, basket, currentProducts ->
            val inBasketCurrentLoadingListId = currentProducts
                .filterIsInstance<ShowcaseItem.Product>()
                .filter(ShowcaseItem.Product::isBasketLoading)
                .map(ShowcaseItem.Product::id)

            products.mapIndexed { index, product ->
                if (product == null) {
                    ShowcaseItem.ProductPlaceHolder(index)
                } else {
                    val bunch = basket.find { basketItem -> basketItem.product.id == product.id }
                    ShowcaseItem.Product(
                        id = product.id,
                        item = product,
                        inBasket = bunch != null,
                        inBasketCount = bunch?.count.orZero(),
                        isBasketLoading = inBasketCurrentLoadingListId.contains(product.id)
                    )
                }
            }
        }

        //Комбинируем поток данных и состояния
        combine(
            productItems,
            pagingList.loadState
        ) { items, states ->

            var isContentLoading = false
            var contentLoadingError: Throwable? = null
            var headerLoader: Boolean = false
            var headerError: Throwable? = null
            var footerLoader: Boolean = false
            var footerError: Throwable? = null

            when (val refresh = states.refresh) {
                is LoadState.NotLoading -> {}
                is LoadState.Loading -> isContentLoading = true
                is LoadState.Error -> contentLoadingError = refresh.error
            }
            when (val prepend = states.prepend) {
                is LoadState.NotLoading -> {}
                is LoadState.Loading -> headerLoader = true
                is LoadState.Error -> headerError = prepend.error
            }
            when (val prepend = states.append) {
                is LoadState.NotLoading -> {}
                is LoadState.Loading -> footerLoader = true
                is LoadState.Error -> footerError = prepend.error
            }

            intent {
                val newItems: List<ShowcaseItem> = items
                    .toMutableList()
                    .apply {
                        if (headerLoader) add(0, ShowcaseItem.Loader)
                        if (headerError != null) add(0, ShowcaseItem.Error(headerError))
                        if (footerLoader) add(ShowcaseItem.Loader)
                        if (footerError != null) add(ShowcaseItem.Error(footerError))
                    }

                reduce {
                    state.copy(
                        items = newItems,
                        isProductsLoading = isContentLoading,
                        productsLoadingError = contentLoadingError
                    )
                }
            }
        }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun retryLoading() {
        pagingList.retry()
    }

    fun onNextItemIndex(index: Int) = intent {
        pagingList.get(index)
    }

    fun addProductToBasket(item: ShowcaseItem.Product) = intent {
        var newItem = item.copy(isBasketLoading = true)
        var newItems = state.items.updateElement(element = newItem, identifier = ShowcaseItem::id)

        reduce { state.copy(items = newItems) }


        if (item.inBasket) {
            val basketItem = BasketBunch(
                product = item.item.convert(),
                count = item.inBasketCount.inc()
            )
            basketRepository.update(basketItem)
        } else {
            val basketItem = BasketBunch(
                product = item.item.convert(),
                count = 1
            )
            delay(MockDelay.MEDIUM)
            basketRepository.add(basketItem)
        }
            .flowOn(Dispatchers.IO)
            .collect()

        newItem = item.copy(isBasketLoading = false)
        newItems = state.items.updateElement(element = newItem, identifier = ShowcaseItem::id)
        reduce { state.copy(items = newItems) }

    }

    fun removeProductFromBasket(item: ShowcaseItem.Product) = intent {
        var newItem = item.copy(isBasketLoading = true)
        var newItems = state.items.updateElement(element = newItem, identifier = ShowcaseItem::id)
        reduce { state.copy(items = newItems) }

        if (item.inBasketCount <= 1) {
            basketRepository.deleteById(item.item.id)
        } else {
            val basketItem = BasketBunch(
                product = item.item.convert(),
                count = item.inBasketCount.dec()
            )
            basketRepository.update(basketItem)
        }
            .flowOn(Dispatchers.IO)
            .collect()

        newItem = item.copy(isBasketLoading = false)
        newItems = state.items.updateElement(element = newItem, identifier = ShowcaseItem::id)
        reduce { state.copy(items = newItems) }
    }

    fun openProductDetails(item: ShowcaseItem.Product) = intent {
        postSideEffect(ShowcaseSideEffect.OpenProductDetails(item.item.id))
    }

    fun openFilters() = intent {
        postSideEffect(ShowcaseSideEffect.ShowContentInDeveloping())
    }

    fun query(value: String) = intent {
        queryFlow.query(value)
        reduce { state.copy(query = value) }
    }

    fun search() = intent {
        queryFlow.execute()
    }

}