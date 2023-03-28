package com.pharmacy.di

import android.app.Application
import com.pharmacy.core.component.Component
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

object DiComponent : Component, KoinComponent {
    override fun init(application: Application) {
        startKoin {
            androidContext(application)
            modules(
                sharedPreferencesModule,
                convertersModule,
                databaseModule,
                localDataSourceModule,
                remoteDataSourceModule,
                repositoriesModule,
                viewModelsModule,
            )
        }
    }
}