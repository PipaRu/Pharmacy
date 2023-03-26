package com.pharmacy.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.basket.db.enitity.BasketBunchEntity
import com.pharmacy.data.source.local.db.adapters.CategoryEntityListConverter
import com.pharmacy.data.source.local.orders.db.dao.OrdersDao
import com.pharmacy.data.source.local.orders.db.enitity.OrderEntity

@TypeConverters(CategoryEntityListConverter::class)
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

object DatabaseFactory {

    fun create(
        context: Context,
        typeConverters: List<Any>,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .apply { typeConverters.forEach(::addTypeConverter) }
            .build()
    }

}