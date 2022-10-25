package com.pharmacy.di

import com.pharmacy.data.source.local.auth.AuthLocalDataSource
import com.pharmacy.data.source.local.auth.AuthLocalDataSourceImpl
import com.pharmacy.data.source.local.basket.BasketLocalDataSource
import com.pharmacy.data.source.local.basket.BasketLocalDataSourceImpl
import com.pharmacy.data.source.local.orders.OrdersLocalDataSource
import com.pharmacy.data.source.local.orders.OrdersLocalDataSourceImpl
import com.pharmacy.data.source.remote.address.AddressRemoteDataSource
import com.pharmacy.data.source.remote.address.AddressRemoteDataSourceImpl
import com.pharmacy.data.source.remote.product.ProductsRemoteDataSource
import com.pharmacy.data.source.remote.product.ProductsRemoteDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataSourceModule = module {

    single<BasketLocalDataSource> {
        BasketLocalDataSourceImpl(
            basketDao = get()
        )
    }

    single<AuthLocalDataSource> {
        AuthLocalDataSourceImpl(
            sharedPreferences = get()
        )
    }

    single<OrdersLocalDataSource> {
        OrdersLocalDataSourceImpl(
            ordersDao = get()
        )
    }

}

val remoteDataSourceModule = module {

    single<ProductsRemoteDataSource> {
        ProductsRemoteDataSourceImpl(
            jsonConverter = get(),
            application = androidApplication()
        )
    }

    single<AddressRemoteDataSource> {
        AddressRemoteDataSourceImpl()
    }

}