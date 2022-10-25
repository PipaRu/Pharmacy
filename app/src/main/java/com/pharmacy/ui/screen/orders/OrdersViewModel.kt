package com.pharmacy.ui.screen.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.extensions.mapElements
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.data.repository.order.OrdersRepository
import com.pharmacy.ui.model.OrderItem
import com.pharmacy.ui.screen.orders.model.mvi.OrdersSideEffect
import com.pharmacy.ui.screen.orders.model.mvi.OrdersViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class OrdersViewModel(
    private val ordersRepository: OrdersRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<OrdersViewState, OrdersSideEffect> {

    override val container = container<OrdersViewState, OrdersSideEffect>(
        initialState = OrdersViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(OrdersSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    init {
        startOrdersLoading()
    }

    fun backAction() = intent {
        postSideEffect(OrdersSideEffect.NavigateBack)
    }

    fun tryAgainLoadingAction() = startOrdersLoading()

    fun selectOrderAction(order: OrderItem) = intent {
        postSideEffect(OrdersSideEffect.ShowContentInDeveloping())
    }

    fun cancelOrderAction(order: OrderItem) = intent {
        postSideEffect(OrdersSideEffect.CancelOrderWarning(order))
    }

    fun cancelOrder(order: OrderItem) = intent {
        ordersRepository.cancelOrder(order.id)
            .flowOn(Dispatchers.IO)
            .collect()
    }

    fun deleteOrder(order: OrderItem) = intent {
        ordersRepository.deleteOrder(order.id)
            .flowOn(Dispatchers.IO)
            .collect()
    }

    private fun startOrdersLoading() = intent {
        ordersRepository.getAllOrders()
            .flowOn(Dispatchers.IO)
            .onStart { onOrdersLoadingStart() }
            .mapElements(OrderItem.Companion::from)
            .onEach { orders -> onOrdersLoaded(orders) }
            .catch { error -> onOrdersLoadingFailed(error) }
            .flowOn(Dispatchers.Default)
            .collect()
    }

    private suspend fun SimpleSyntax<OrdersViewState, OrdersSideEffect>.onOrdersLoadingStart() {
        reduce {
            state.copy(
                isContentLoading = true,
                isContentLoadingFailed = false
            )
        }
    }

    private suspend fun SimpleSyntax<OrdersViewState, OrdersSideEffect>.onOrdersLoaded(
        orders: List<OrderItem>
    ) {
        reduce {
            state.copy(
                isContentLoading = false,
                isContentLoadingFailed = false,
                orders = orders
            )
        }
    }

    private suspend fun SimpleSyntax<OrdersViewState, OrdersSideEffect>.onOrdersLoadingFailed(
        error: Throwable
    ) {
        reduce {
            state.copy(
                isContentLoadingFailed = true,
                isContentLoading = false
            )
        }
    }

}