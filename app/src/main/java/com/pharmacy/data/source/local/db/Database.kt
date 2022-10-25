package com.pharmacy.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.basket.db.enitity.BasketBunchEntity
import com.pharmacy.data.source.local.orders.db.dao.OrdersDao
import com.pharmacy.data.source.local.orders.db.enitity.OrderEntity

@Database(
    entities = [BasketBunchEntity::class, OrderEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val basket: BasketDao
    abstract val orders: OrdersDao

    companion object {
        const val DATABASE_NAME = "pharmacy-db"
    }
}