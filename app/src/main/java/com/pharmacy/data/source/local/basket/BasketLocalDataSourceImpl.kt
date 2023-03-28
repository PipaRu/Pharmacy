package com.pharmacy.data.source.local.basket

import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.extensions.flowOf
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.basket.db.enitity.BasketBunchEntity
import com.pharmacy.di.DiComponent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.koin.core.component.get

class BasketLocalDataSourceImpl : BasketLocalDataSource {

    private val basketDao: BasketDao
        get() = DiComponent.get<BasketDao>()

    override fun add(value: BasketBunch): Flow<Unit> {
        return flowOf { BasketBunchEntity.from(value) }
            .map { entities -> basketDao.insert(entities) }
    }

    override fun addAll(values: Set<BasketBunch>): Flow<Unit> {
        return flowOf { values.map(BasketBunchEntity.Companion::from) }
            .map { entities -> basketDao.insertAll(entities.toSet()) }
    }

    override fun update(value: BasketBunch): Flow<Unit> {
        return flowOf { BasketBunchEntity.from(value) }
            .map { entity -> basketDao.update(entity) }
    }

    override fun updateAll(values: Set<BasketBunch>): Flow<Unit> {
        return flowOf { values.map(BasketBunchEntity.Companion::from) }
            .map { entities -> basketDao.updateAll(entities.toSet()) }
    }

    override fun delete(value: BasketBunch): Flow<Unit> {
        return flowOf { BasketBunchEntity.from(value) }
            .map { entity -> basketDao.delete(entity) }
    }

    override fun deleteById(id: Int): Flow<Unit> {
        return flowOf { basketDao.deleteById(id) }
    }

    override fun deleteAll(values: Set<BasketBunch>): Flow<Unit> {
        return flowOf { values.map(BasketBunchEntity.Companion::from) }
            .map { entities -> basketDao.deleteAll(entities.toSet()) }
    }

    override fun deleteAllById(values: Set<Int>): Flow<Unit> {
        return flowOf { basketDao.deleteAllById(values) }
    }

    private val refresh = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun getAll(): Flow<List<BasketBunch>> {
        return refresh
            .onStart { emit(Unit) }
            .flatMapLatest {
                basketDao.observe()
                    .map { entities -> entities.map(BasketBunchEntity::convert) }
            }
    }

    override fun refresh(): Flow<Unit> {
        return flowOf { refresh.emit(Unit) }
    }

    override fun clear(): Flow<Unit> {
        return flowOf { basketDao.clear() }
    }

}