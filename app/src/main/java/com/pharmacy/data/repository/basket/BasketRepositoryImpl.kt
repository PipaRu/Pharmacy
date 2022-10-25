package com.pharmacy.data.repository.basket

import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.source.local.basket.BasketLocalDataSource
import kotlinx.coroutines.flow.Flow

class BasketRepositoryImpl(
    private val localDataSource: BasketLocalDataSource,
) : BasketRepository {

    override fun add(value: BasketBunch): Flow<Unit> = localDataSource.add(value)

    override fun addAll(values: Set<BasketBunch>): Flow<Unit> = localDataSource.addAll(values)

    override fun update(value: BasketBunch): Flow<Unit> = localDataSource.update(value)

    override fun updateAll(values: Set<BasketBunch>): Flow<Unit> = localDataSource.updateAll(values)

    override fun delete(value: BasketBunch): Flow<Unit> = localDataSource.delete(value)

    override fun deleteById(id: Int): Flow<Unit> = localDataSource.deleteById(id)

    override fun deleteAll(values: Set<BasketBunch>): Flow<Unit> = localDataSource.deleteAll(values)

    override fun deleteAllById(ids: Set<Int>): Flow<Unit> = localDataSource.deleteAllById(ids)

    override fun getAll(): Flow<List<BasketBunch>> = localDataSource.getAll()

    override fun clear(): Flow<Unit> = localDataSource.clear()

}