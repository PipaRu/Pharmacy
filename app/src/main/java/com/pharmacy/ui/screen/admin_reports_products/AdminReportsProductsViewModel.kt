package com.pharmacy.ui.screen.admin_reports_products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.coroutines.flow.query.QueryFlow
import com.pharmacy.common.extensions.mapElements
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.*
import com.pharmacy.ui.screen.admin_reports_products.model.mvi.AdminReportsProductsSideEffect
import com.pharmacy.ui.screen.admin_reports_products.model.mvi.AdminReportsProductsViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class AdminReportsProductsViewModel(
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AdminReportsProductsViewState, AdminReportsProductsSideEffect> {

    companion object {
        private const val QUERY_DEBOUNCE_MILLISECONDS = 1_000L
    }

    private val allProductStatistics: StateFlow<List<ProductStatistic>> = productsRepository.fetchProducts(
        position = 0,
        count = Int.MAX_VALUE,
    )
        .mapElements(ProductItem.Companion::from)
        .mapElements { item ->
            ProductStatistic(
                product = item,
                totalOrdersCount = (10..500).random(),
                ordersInWeek = (10..100).random(),
                totalProfit = Random.nextDouble(500.0, 5_000.0),
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override val container = container<AdminReportsProductsViewState, AdminReportsProductsSideEffect>(
        initialState = AdminReportsProductsViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler,
        ),
    )

    private val queryFlow: QueryFlow = QueryFlow(
        debounce = QUERY_DEBOUNCE_MILLISECONDS.milliseconds,
        coroutineScope = viewModelScope,
    )

    init {
        intent {
            repeatOnSubscription {
                queryFlow.pending
                    .onEach { queryValue ->
                        reduce {
                            state.copy(query = queryValue)
                        }
                    }
                    .collect()
            }
        }
        intent {
            repeatOnSubscription {
                reduce {
                    state.copy(
                        query = null,
                        products = Resource.Loading,
                        selectedProductStatistic = Resource.Nothing,
                        isInitialLoading = true,
                    )
                }
                delay(MockDelay.SMALL)
                combine(
                    allProductStatistics,
                    queryFlow,
                ) { statisticList, query ->
                    reduce {
                        state.copy(
                            query = query,
                            products = Resource.Loading,
                            selectedProductStatistic = Resource.Nothing,
                        )
                    }
                    delay(MockDelay.MEDIUM)
                    val categories = statisticList
                        .filter { statistics ->
                            if (query == null) true
                            else statistics.product.name.contains(query, ignoreCase = true)
                        }
                        .map(ProductStatistic::product)
                        .toParcelableList()
                    reduce {
                        state.copy(
                            isInitialLoading = false,
                            products = Resource.Data(categories),
                        )
                    }
                }.collect()
            }
        }
    }


    fun back() = intent {
        if (state.selectedProductStatistic !is Resource.Nothing) {
            reduce { state.copy(selectedProductStatistic = Resource.Nothing) }
        } else {
            postSideEffect(AdminReportsProductsSideEffect.NavigateBack)
        }
    }

    fun selectProduct(product: ProductItem) = intent {
        val statistic = allProductStatistics.value.find { it.product.id == product.id } ?: return@intent
        reduce { state.copy(selectedProductStatistic = Resource.Data(statistic)) }
    }

    fun changeQuery(query: String) = intent {
        queryFlow.query(query)
    }

    fun search() = intent {
        queryFlow.execute()
    }

}