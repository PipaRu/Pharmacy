package com.pharmacy.core.crashlytics

import android.app.Application
import com.pharmacy.core.component.Component

object CrashlyticsComponent : Component {
    override fun init(application: Application) {
        Crashlytics.setInstance(DefaultCrashlytics())
    }
}