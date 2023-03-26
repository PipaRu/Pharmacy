@file:OptIn(ExperimentalCoroutinesApi::class)

package com.pharmacy.ui.screen.admin_products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.coroutines.flow.query.QueryFlow
import com.pharmacy.common.extensions.filter
import com.pharmacy.common.extensions.mapElements
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.ProductFilter
import com.pharmacy.data.repository.admin_products.AdminProductsRepository
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.model.SelectableItem
import com.pharmacy.ui.screen.admin_products.model.mvi.AdminProductsSideEffect
import com.pharmacy.ui.screen.admin_products.model.mvi.AdminProductsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class AdminProductsViewModel(
    private val productsRepository: ProductsRepository,
    private val adminProductsRepository: AdminProductsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<AdminProductsViewState, AdminProductsSideEffect> {

    companion object {
        private const val QUERY_DEBOUNCE_MILLISECONDS = 1_000L
    }

    override val container = container<AdminProductsViewState, AdminProductsSideEffect>(
        initialState = AdminProductsViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(AdminProductsSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    private val queryFlow = QueryFlow(
        debounceMilliseconds = QUERY_DEBOUNCE_MILLISECONDS,
        coroutineScope = viewModelScope
    )

    init {

        val queryFlow1 = queryFlow
            .map(::ProductFilter)
            .flowOn(Dispatchers.Default)
        val allProductsFlow = productsRepository.allProducts
        combine(
            queryFlow1, allProductsFlow
        ) { filter, allProducts ->
            adminProductsRepository.getAllProducts(filter = filter)
        }
            .flattenConcat()
            .flowOn(Dispatchers.IO)
            .mapElements(ProductItem.Companion::from)
            .mapElements(::SelectableItem)
            .flowOn(Dispatchers.Default)
            .onEach { items ->
                intent {
                    reduce {
                        state.copy(items = items, isContentLoading = false)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onProductClick(productItem: ProductItem) = intent {
        postSideEffect(AdminProductsSideEffect.NavigateToProductDetails(productItem))
    }

    fun onSelectProduct(productItem: ProductItem) = intent {
        val currentItems = state.items
        val newItems = currentItems.map { item ->
            if (productItem.id == item.value.id) {
                item.copy(isSelected = !item.isSelected)
            } else {
                item
            }
        }
        reduce { state.copy(items = newItems) }
    }

    fun back() = intent {
        postSideEffect(AdminProductsSideEffect.NavigateBack)
    }

    fun setSelectionMode(enabled: Boolean) = intent {
        reduce { state.copy(isSelectionMode = enabled) }
    }

    fun deleteAllSelectedAction() = intent {
        postSideEffect(AdminProductsSideEffect.ShowDeleteAllSelectedProductsWarning)
    }

    fun deleteAllSelected() = intent {
        reduce { state.copy(isContentEditingLoading = true) }
        val forDeletion = state.items
            .filter { item -> item.isSelected }
            .map { item -> item.value.convert() }
            .toSet()
        delay(MockDelay.MEDIUM)
        adminProductsRepository.deleteProducts(forDeletion)
            .flowOn(Dispatchers.IO)
            .collect()
        val newItems = state.items.filter { item -> !item.isSelected }
        reduce { state.copy(isContentEditingLoading = false, items = newItems) }
    }

    fun onQueryChanged(query: String) = intent {
        queryFlow.query(query)
        reduce { state.copy(query = query) }
    }

    fun search() = intent {
        queryFlow.execute()
    }

    fun addProduct() = intent {
        postSideEffect(AdminProductsSideEffect.NavigateToProductAdding)
    }

}