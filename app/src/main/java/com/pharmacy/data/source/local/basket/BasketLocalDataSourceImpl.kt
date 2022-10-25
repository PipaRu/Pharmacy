package com.pharmacy.data.source.local.basket

import com.pharmacy.common.converter.model.convert
import com.pharmacy.common.converter.model.from
import com.pharmacy.common.extensions.flowOf
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.basket.db.enitity.BasketBunchEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BasketLocalDataSourceImpl(
    private val basketDao: BasketDao,
) : BasketLocalDataSource {

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

    override fun getAll(): Flow<List<BasketBunch>> {
        return basketDao.observe()
            .map { entities -> entities.map(BasketBunchEntity::convert) }
    }

    override fun clear(): Flow<Unit> {
        return flowOf { basketDao.clear() }
    }

}