@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.pharmacy.ui.screen.products_filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.CategoryItem
import com.pharmacy.ui.model.SelectableItem
import com.pharmacy.ui.screen.products_filter.model.PriceRange
import com.pharmacy.ui.screen.products_filter.model.mvi.ProductsFilterSideEffect
import com.pharmacy.ui.screen.products_filter.model.mvi.ProductsFilterViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class ProductsFilterViewModel(
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<ProductsFilterViewState, ProductsFilterSideEffect> {

    override val container = container<ProductsFilterViewState, ProductsFilterSideEffect>(
        initialState = ProductsFilterViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(ProductsFilterSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    private val query = MutableStateFlow<String?>(null)
    private val categories = MutableStateFlow<List<SelectableItem<CategoryItem>>>(emptyList())
    private val priceRange = MutableStateFlow(PriceRange.Empty)
    private val loading = MutableStateFlow<Boolean>(true)
    private val filterFlow = MutableStateFlow<ProductFilter?>(null)

    private var stateJob: Job? = null

    private fun initStateJob() {
        val priceRangeFlow = productsRepository.getProductPriceRange()
            .flowOn(Dispatchers.IO)
            .map { range ->
                PriceRange(
                    start = range.first,
                    end = range.last,
                    currentStart = 0,
                    currentEnd = 0,
                )
            }
            .onEach(priceRange::emit)
            .flatMapConcat { priceRange }
            .flowOn(Dispatchers.Default)

        val categoriesFlow = productsRepository.getProductCategories()
            .flowOn(Dispatchers.IO)
            .map { it.map(CategoryItem.Companion::from) }
            .map { it.map(::SelectableItem) }
            .onEach(categories::emit)
            .flatMapConcat { categories }
            .flowOn(Dispatchers.Default)

        val filterFlow = combine(query, categoriesFlow, priceRangeFlow) { query, categories, priceRange ->
            ProductFilter(
                query = query,
                priceRange = priceRange.currentStart.rangeTo(priceRange.currentEnd),
                categories = categories
                    .filter(SelectableItem<CategoryItem>::isSelected)
                    .map { it.value.convert() }
            )
        }
            .onEach(filterFlow::emit)

        val productCountFlow = filterFlow.flatMapLatest(productsRepository::getProductCount)

        stateJob = combine(
            query,
            loading,
            categoriesFlow,
            priceRangeFlow,
            productCountFlow
        ) { query, isLoading, categories, priceRange, productCount ->
            ProductsFilterViewState(
                query = query,
                isLoading = isLoading,
                priceRange = priceRange,
                categories = categories,
                productCount = productCount,
            )
        }
            .onEach { newState ->
                intent {
                    reduce { newState }
                }
            }
            .launchIn(viewModelScope)
    }

    init {
        initStateJob()
    }

    fun onCategoryClicked(category: CategoryItem) = intent {
        val currentCategories = categories.value
        val newCategories = currentCategories.map {
            if (it.value.name == category.name) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it
            }
        }
        categories.emit(newCategories)
    }

    fun onPriceRangeChanged(start: Int, end: Int) = intent {
        val currentPriceRange = priceRange.value
        val newPriceRange = currentPriceRange.copy(
            currentStart = start,
            currentEnd = end,
        )
        priceRange.emit(newPriceRange)
    }

    fun back() = intent {
        postSideEffect(ProductsFilterSideEffect.NavigateBack)
    }

    fun reset() = intent {
        stateJob?.cancel()
        initStateJob()
    }

    fun apply() = intent {
        postSideEffect(ProductsFilterSideEffect.ApplyFilter(filterFlow.value))
    }

}