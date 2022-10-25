package com.pharmacy

import android.app.Application
import com.pharmacy.core.component.Component.Companion.initializeComponents
import com.pharmacy.core.crashlytics.CrashlyticsComponent
import com.pharmacy.core.logger.LoggerComponent
import com.pharmacy.core.theme.ThemeComponent
import com.pharmacy.di.DiComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeComponents(
            DiComponent,
            LoggerComponent,
            CrashlyticsComponent,
            ThemeComponent
        )
    }

}