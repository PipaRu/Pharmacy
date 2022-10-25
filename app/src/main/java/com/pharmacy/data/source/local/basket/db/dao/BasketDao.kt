package com.pharmacy.data.source.local.basket.db.dao

import androidx.room.*
import com.pharmacy.data.source.local.basket.db.enitity.BasketBunchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: BasketBunchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: Set<BasketBunchEntity>)

    @Delete
    suspend fun delete(product: BasketBunchEntity)

    @Query("DELETE FROM basket WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Delete
    suspend fun deleteAll(products: Set<BasketBunchEntity>)

    @Query("DELETE FROM basket WHERE id in (:ids)")
    suspend fun deleteAllById(ids: Set<Int>)

    @Update
    suspend fun update(product: BasketBunchEntity)

    @Update
    suspend fun updateAll(products: Set<BasketBunchEntity>)

    @Query("SELECT * FROM basket")
    suspend fun getAll(): List<BasketBunchEntity>

    @Query("DELETE FROM basket")
    suspend fun clear()

    @Query("SELECT * FROM basket")
    fun observe(): Flow<List<BasketBunchEntity>>

}