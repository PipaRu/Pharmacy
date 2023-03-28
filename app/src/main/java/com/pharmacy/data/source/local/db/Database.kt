package com.pharmacy.data.source.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.basket.db.enitity.BasketBunchEntity
import com.pharmacy.data.source.local.db.adapters.CategoryEntityListConverter
import com.pharmacy.data.source.local.orders.db.dao.OrdersDao
import com.pharmacy.data.source.local.orders.db.enitity.OrderEntity
import com.pharmacy.di.DiComponent
import org.koin.core.component.get

@TypeConverters(CategoryEntityListConverter::class)
@Database(
    entities = [BasketBunchEntity::class, OrderEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val basket: BasketDao
    abstract val orders: OrdersDao
}

@Deprecated("Костыль для разграничения БД между пользователями")
object DatabaseFactory {

    private const val DATABASE_NAME_PREFIX = "pharmacy-db"
    const val DATABASE_NAME_ANON = "anon"

    private var currentDatabaseName: String = DATABASE_NAME_ANON
    private val databaseMap: MutableMap<String, AppDatabase> = mutableMapOf()

    fun get(): AppDatabase {
        return create(currentDatabaseName)
    }

    fun create(
        name: String = DATABASE_NAME_ANON,
    ): AppDatabase {
        return databaseMap.getOrPut(name) {
            val converters = listOf<Any>(
                CategoryEntityListConverter(
                    jsonConverter = DiComponent.get(),
                )
            )
            Room.databaseBuilder(
                DiComponent.get(),
                AppDatabase::class.java,
                "$DATABASE_NAME_PREFIX-$name",
            )
                .fallbackToDestructiveMigration()
                .apply { converters.forEach(::addTypeConverter) }
                .build()
        }
            .also { currentDatabaseName = name }
    }

}