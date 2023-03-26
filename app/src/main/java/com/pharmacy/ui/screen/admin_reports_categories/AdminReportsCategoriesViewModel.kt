package com.pharmacy.ui.screen.admin_reports_categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.coroutines.flow.query.QueryFlow
import com.pharmacy.common.extensions.mapElements
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.model.Category
import com.pharmacy.data.repository.address.AddressRepository
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.ui.model.*
import com.pharmacy.ui.screen.admin_reports_categories.model.mvi.AdminReportsCategoriesSideEffect
import com.pharmacy.ui.screen.admin_reports_categories.model.mvi.AdminReportsCategoriesViewState
import kotlinx.coroutines.CoroutineExceptionHandler
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

class AdminReportsCategoriesViewModel(
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AdminReportsCategoriesViewState, AdminReportsCategoriesSideEffect> {

    companion object {
        private const val QUERY_DEBOUNCE_MILLISECONDS = 1_000L
    }

    private val allCategoryStatistics: StateFlow<List<CategoryStatistic>> = productsRepository.getProductCategories()
        .mapElements(CategoryItem.Companion::from)
        .mapElements { item ->
            CategoryStatistic(
                category = item,
                productsCount = (5..100).random(),
                totalOrdersCount = (100..1000).random(),
                ordersInWeek = (10..100).random(),
                totalProfit = Random.nextDouble(500.0, 5_000.0),
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    override val container = container<AdminReportsCategoriesViewState, AdminReportsCategoriesSideEffect>(
        initialState = AdminReportsCategoriesViewState(),
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
                            state.copy(
                                query = queryValue,
                            )
                        }
                    }
                    .collect()
            }
        }
        intent {
            repeatOnSubscription {
                reduce {
                    state.copy(
                        isInitialLoading = true,
                        categories = Resource.Loading,
                        selectedCategoryStatistic = Resource.Nothing,
                    )
                }
                delay(MockDelay.SMALL)
                combine(
                    allCategoryStatistics,
                    queryFlow,
                ) { statisticList, query ->
                    reduce {
                        state.copy(
                            categories = Resource.Loading,
                            query = query,
                        )
                    }
                    delay(MockDelay.MEDIUM)
                    val categories = statisticList
                        .filter { statistics ->
                            if (query == null) true
                            else statistics.category.name.contains(query, ignoreCase = true)
                        }
                        .map(CategoryStatistic::category)
                        .toParcelableList()
                    reduce {
                        state.copy(
                            categories = Resource.Data(categories),
                            isInitialLoading = false,
                        )
                    }
                }.collect()
            }
        }
    }


    fun back() = intent {
        if (state.selectedCategoryStatistic !is Resource.Nothing) {
            reduce { state.copy(selectedCategoryStatistic = Resource.Nothing) }
        } else {
            postSideEffect(AdminReportsCategoriesSideEffect.NavigateBack)
        }
    }

    fun selectCategory(category: CategoryItem) = intent {
        val statistic = allCategoryStatistics.value.find { it.category.name == category.name } ?: return@intent
        reduce { state.copy(selectedCategoryStatistic = Resource.Data(statistic)) }
    }

    fun changeQuery(query: String) = intent {
        queryFlow.query(query)
    }

    fun search() = intent {
        queryFlow.execute()
    }

}