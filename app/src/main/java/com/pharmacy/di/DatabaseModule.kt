package com.pharmacy.di

import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.db.DatabaseFactory
import com.pharmacy.data.source.local.orders.db.dao.OrdersDao
import org.koin.dsl.module

val databaseModule = module {

//    single<AppDatabase> {
//        val converters = listOf<Any>(
//            CategoryEntityListConverter(
//                jsonConverter = get(),
//            )
//        )
//        DatabaseFactory.create(
//            context = androidApplication(),
//            typeConverters = converters,
//        )
//    }
//
//    factory<AppDatabase>(qualifier = named("Factory")) { parametersHolder ->
//        val converters = listOf<Any>(
//            CategoryEntityListConverter(
//                jsonConverter = get(),
//            )
//        )
//        DatabaseFactory.create(
//            name = parametersHolder.get<String>(),
//            context = androidApplication(),
//            typeConverters = converters,
//        )
//    }

    factory<BasketDao> { DatabaseFactory.get().basket }

    factory<OrdersDao> { DatabaseFactory.get().orders }

}