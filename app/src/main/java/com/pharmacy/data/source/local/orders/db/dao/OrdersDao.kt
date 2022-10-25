package com.pharmacy.data.source.local.orders.db.dao

import androidx.room.*
import com.pharmacy.data.source.local.orders.db.enitity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: Set<OrderEntity>)

    @Delete
    suspend fun delete(order: OrderEntity)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteAll(orders: Set<OrderEntity>)

    @Query("DELETE FROM orders WHERE id in (:ids)")
    suspend fun deleteAllById(ids: Set<Long>)

    @Update
    suspend fun update(order: OrderEntity)

    @Update
    suspend fun updateAll(orders: Set<OrderEntity>)

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun get(id: Long): OrderEntity

    @Query("SELECT * FROM orders")
    suspend fun getAll(): List<OrderEntity>

    @Query("DELETE FROM orders")
    suspend fun clear()

    @Query("SELECT * FROM orders")
    fun observe(): Flow<List<OrderEntity>>

}