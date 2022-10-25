package com.pharmacy.ui.screen.basket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.coroutines.flow.query.QueryFlow
import com.pharmacy.common.extensions.filter
import com.pharmacy.common.extensions.mapElements
import com.pharmacy.common.filter.Filter
import com.pharmacy.common.filter.Filters
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.model.UserStatus
import com.pharmacy.data.repository.auth.AuthRepository
import com.pharmacy.data.repository.basket.BasketRepository
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.SelectableItem
import com.pharmacy.ui.screen.basket.model.mvi.BasketSideEffect
import com.pharmacy.ui.screen.basket.model.mvi.BasketViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class BasketViewModel(
    private val basketRepository: BasketRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<BasketViewState, BasketSideEffect> {

    companion object {
        private const val QUERY_DEBOUNCE_MILLISECONDS = 1_000L
    }

    override val container = container<BasketViewState, BasketSideEffect>(
        initialState = BasketViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(BasketSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    private val queryFlow: QueryFlow = QueryFlow(
        debounceMilliseconds = QUERY_DEBOUNCE_MILLISECONDS,
        coroutineScope = viewModelScope
    )

    init {
        val filterFlow: Flow<Filter<BasketBunch>> = queryFlow
            .map<String, Filter<BasketBunch>> { query ->
                if (query.isEmpty()) return@map Filters.disabled()
                val id = query.toIntOrNull()
                if (id != null) {
                    Filters.create { product -> product.id == id }
                } else {
                    Filters.create { item ->
                        item.product.name.contains(other = query, ignoreCase = true)
                                || item.product.description.contains(query, ignoreCase = true)
                    }
                }
            }
            .flowOn(Dispatchers.Default)
        val productsFlow = basketRepository.getAll()
            .flowOn(Dispatchers.IO)

        combine(productsFlow, filterFlow, List<BasketBunch>::filter)
            .flowOn(Dispatchers.Default)
            .onStart { delay(MockDelay.MEDIUM) }
            .mapElements(BasketBunchItem.Companion::from)
            .mapElements(::SelectableItem)
            .onEach { items ->
                intent {
                    reduce {
                        state.copy(items = items, isProductsLoading = false)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onProductClicked(product: BasketBunchItem) = intent {
        postSideEffect(BasketSideEffect.ShowContentInDeveloping())
    }

    fun onProductSelected(product: BasketBunchItem, isSelected: Boolean) = intent {
        val elements = state.items.map { element ->
            if (element.value.id == product.id) {
                element.copy(isSelected = isSelected)
            } else {
                element
            }
        }
        reduce { state.copy(items = elements) }
    }

    fun deleteAllSelected() = intent {
        val selected = state.items
            .filter { it.isSelected }
            .map { it.value.id }
            .toSet()
        if (selected.isNotEmpty()) {
            basketRepository.deleteAllById(selected)
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }

    fun addProduct(product: BasketBunchItem) = intent {
        val basketBunch = product.convert().copy(
            count = product.count.inc()
        )
        basketRepository.update(basketBunch)
            .flowOn(Dispatchers.IO)
            .collect()
    }

    fun removeProduct(product: BasketBunchItem) = intent {
        if (product.count <= 1) {
            basketRepository.deleteById(product.id)
        } else {
            val basketBunch = product.convert().copy(
                count = product.count.dec()
            )
            basketRepository.update(basketBunch)
        }
            .flowOn(Dispatchers.IO)
            .collect()
    }

    fun tryBuyAllSelected() = intent {
        reduce { state.copy(isPurchaseProcessLoading = true) }
        val products = state.items
            .filter(SelectableItem<BasketBunchItem>::isSelected)
            .map(SelectableItem<BasketBunchItem>::value)
        when (authRepository.userStatus.first()) {
            is UserStatus.Unauthorized -> {
                postSideEffect(BasketSideEffect.ShowAuthorizationRequired)
            }
            is UserStatus.Admin -> {
                postSideEffect(BasketSideEffect.ShowCheckout(products))
            }
            is UserStatus.User -> {
                postSideEffect(BasketSideEffect.ShowCheckout(products))
            }
        }
        reduce { state.copy(isPurchaseProcessLoading = false) }
    }

    fun query(query: String) = intent {
        queryFlow.query(query)
        reduce { state.copy(query = query) }
    }

    fun search() = intent {
        queryFlow.execute()
    }

    fun openFilters() = intent {
        postSideEffect(BasketSideEffect.ShowContentInDeveloping())
    }

}