package com.pharmacy.di

import com.pharmacy.data.repository.address.AddressRepository
import com.pharmacy.data.repository.address.AddressRepositoryImpl
import com.pharmacy.data.repository.admin.AdminRepository
import com.pharmacy.data.repository.admin.AdminRepositoryImpl
import com.pharmacy.data.repository.admin_products.AdminProductsRepository
import com.pharmacy.data.repository.admin_products.AdminProductsRepositoryImpl
import com.pharmacy.data.repository.auth.AuthRepository
import com.pharmacy.data.repository.auth.AuthRepositoryImpl
import com.pharmacy.data.repository.basket.BasketRepository
import com.pharmacy.data.repository.basket.BasketRepositoryImpl
import com.pharmacy.data.repository.order.OrdersRepository
import com.pharmacy.data.repository.order.OrdersRepositoryImpl
import com.pharmacy.data.repository.product.ProductsRepository
import com.pharmacy.data.repository.product.ProductsRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {

    single<ProductsRepository> {
        ProductsRepositoryImpl(
            remoteDataSource = get()
        )
    }

    single<BasketRepository> {
        BasketRepositoryImpl(
            localDataSource = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            localDataSource = get()
        )
    }

    single<AdminRepository> {
        AdminRepositoryImpl()
    }

    single<AdminProductsRepository> {
        AdminProductsRepositoryImpl(
            remoteDataSource = get()
        )
    }

    single<AddressRepository> {
        AddressRepositoryImpl(
            remoteDataSource = get()
        )
    }

    single<OrdersRepository> {
        OrdersRepositoryImpl(
            localDataSource = get()
        )
    }

}