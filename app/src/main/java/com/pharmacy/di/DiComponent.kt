package com.pharmacy.di

import android.app.Application
import com.pharmacy.core.component.Component
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object DiComponent : Component {
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