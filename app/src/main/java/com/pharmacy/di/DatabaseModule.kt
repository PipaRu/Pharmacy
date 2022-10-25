package com.pharmacy.di

import androidx.room.Room
import com.pharmacy.data.source.local.basket.db.dao.BasketDao
import com.pharmacy.data.source.local.db.AppDatabase
import com.pharmacy.data.source.local.orders.db.dao.OrdersDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    factory<BasketDao> { get<AppDatabase>().basket }

    factory<OrdersDao> { get<AppDatabase>().orders }

}