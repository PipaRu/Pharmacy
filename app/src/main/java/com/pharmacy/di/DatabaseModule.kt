package com.pharmacy.di

import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.db.AppDatabase
import com.pharmacy.data.source.local.db.DatabaseFactory
import com.pharmacy.data.source.local.db.adapters.CategoryEntityListConverter
import com.pharmacy.data.source.local.orders.db.dao.OrdersDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        val converters = listOf<Any>(
            CategoryEntityListConverter(
                jsonConverter = get(),
            )
        )
        DatabaseFactory.create(
            context = androidApplication(),
            typeConverters = converters,
        )
    }

    factory<BasketDao> { get<AppDatabase>().basket }

    factory<OrdersDao> { get<AppDatabase>().orders }

}