package com.pharmacy.data.source.local.orders

import com.pharmacy.data.model.Order
import kotlinx.coroutines.flow.Flow

interface OrdersLocalDataSource {

    fun get(id: Long): Flow<Order>

    fun add(value: Order): Flow<Unit>

    fun addAll(values: Set<Order>): Flow<Unit>

    fun update(value: Order): Flow<Unit>

    fun updateAll(values: Set<Order>): Flow<Unit>

    fun delete(value: Order): Flow<Unit>

    fun deleteById(id: Long): Flow<Unit>

    fun deleteAll(values: Set<Order>): Flow<Unit>

    fun deleteAllById(values: Set<Long>): Flow<Unit>

    fun getAll(): Flow<List<Order>>

    fun refresh(): Flow<Unit>

    fun clear(): Flow<Unit>

}