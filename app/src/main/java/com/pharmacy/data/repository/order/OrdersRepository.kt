package com.pharmacy.data.repository.order

import com.pharmacy.data.model.Address
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.model.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

    fun getAllOrders(): Flow<List<Order>>

    fun createOrder(products: List<BasketBunch>, address: Address): Flow<Order>

    fun cancelOrder(id: Long): Flow<Unit>

    fun deleteOrder(id: Long): Flow<Unit>

}