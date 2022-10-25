package com.pharmacy.data.repository.basket

import com.pharmacy.data.model.BasketBunch
import kotlinx.coroutines.flow.Flow

interface BasketRepository {

    fun add(value: BasketBunch): Flow<Unit>

    fun addAll(values: Set<BasketBunch>): Flow<Unit>

    fun update(value: BasketBunch): Flow<Unit>

    fun updateAll(values: Set<BasketBunch>): Flow<Unit>

    fun delete(value: BasketBunch): Flow<Unit>

    fun deleteById(id: Int): Flow<Unit>

    fun deleteAll(values: Set<BasketBunch>): Flow<Unit>

    fun deleteAllById(ids: Set<Int>): Flow<Unit>

    fun getAll(): Flow<List<BasketBunch>>

    fun clear(): Flow<Unit>

}