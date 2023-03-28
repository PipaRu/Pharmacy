package com.pharmacy.data.source.local.orders

import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.extensions.flowOf
import com.pharmacy.data.model.Order
import com.pharmacy.data.source.local.orders.db.dao.OrdersDao
import com.pharmacy.data.source.local.orders.db.enitity.OrderEntity
import com.pharmacy.di.DiComponent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.koin.core.component.get

class OrdersLocalDataSourceImpl : OrdersLocalDataSource {

    private val ordersDao: OrdersDao
        get() = DiComponent.get()

    override fun get(id: Long): Flow<Order> {
        return flowOf { ordersDao.get(id) }.map(OrderEntity::convert)
    }

    override fun add(value: Order): Flow<Unit> {
        return flowOf { OrderEntity.from(value) }
            .map { entities -> ordersDao.insert(entities) }
    }

    override fun addAll(values: Set<Order>): Flow<Unit> {
        return flowOf { values.map(OrderEntity.Companion::from) }
            .map { entities -> ordersDao.insertAll(entities.toSet()) }
    }

    override fun update(value: Order): Flow<Unit> {
        return flowOf { OrderEntity.from(value) }
            .map { entity -> ordersDao.update(entity) }
    }

    override fun updateAll(values: Set<Order>): Flow<Unit> {
        return flowOf { values.map(OrderEntity.Companion::from) }
            .map { entities -> ordersDao.updateAll(entities.toSet()) }
    }

    override fun delete(value: Order): Flow<Unit> {
        return flowOf { OrderEntity.from(value) }
            .map { entity -> ordersDao.delete(entity) }
    }

    override fun deleteById(id: Long): Flow<Unit> {
        return flowOf { ordersDao.deleteById(id) }
    }

    override fun deleteAll(values: Set<Order>): Flow<Unit> {
        return flowOf { values.map(OrderEntity.Companion::from) }
            .map { entities -> ordersDao.deleteAll(entities.toSet()) }
    }

    override fun deleteAllById(values: Set<Long>): Flow<Unit> {
        return flowOf { ordersDao.deleteAllById(values) }
    }


    private val refresh = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun getAll(): Flow<List<Order>> {
        return refresh
            .onStart { emit(Unit) }
            .flatMapLatest {
                ordersDao.observe()
                    .map { entities -> entities.map(OrderEntity::convert) }
            }
    }

    override fun refresh(): Flow<Unit> {
        return flowOf { refresh.emit(Unit) }
    }

    override fun clear(): Flow<Unit> {
        return flowOf { ordersDao.clear() }
    }

}