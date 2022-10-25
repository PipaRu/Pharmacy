package com.pharmacy.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val APP_PREFERENCES = "application_preferences"

val sharedPreferencesModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

}