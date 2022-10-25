@file:OptIn(ExperimentalCoroutinesApi::class)

package com.pharmacy.data.repository.order

import com.pharmacy.common.extensions.flowOf
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.data.model.Address
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.model.Order
import com.pharmacy.data.source.local.orders.OrdersLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.util.*
import kotlin.random.Random

class OrdersRepositoryImpl(
    private val localDataSource: OrdersLocalDataSource,
) : OrdersRepository {

    override fun getAllOrders(): Flow<List<Order>> {
        return localDataSource.getAll()
    }

    override fun createOrder(
        products: List<BasketBunch>,
        address: Address,
    ): Flow<Order> = flowOf {
        Order(
            id = Random.nextLong(1, 1_000_000),
            date = Date(),
            status = Order.Status.CREATED
        )
    }
        .flatMapLatest { order -> localDataSource.add(order).map { order } }
        .onEach { delay(MockDelay.LONG) }
        .map { order -> order.copy(status = Order.Status.PROGRESS) }
        .flatMapLatest { order -> localDataSource.update(order).map { order } }

    override fun cancelOrder(id: Long): Flow<Unit> {
        return localDataSource.get(id)
            .map { order -> order.copy(status = Order.Status.CANCELLED) }
            .flatMapLatest { order -> localDataSource.update(order) }
    }

    override fun deleteOrder(id: Long): Flow<Unit> {
        return localDataSource.deleteById(id)
    }

}