package com.pharmacy

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.pharmacy.core.component.Component.Companion.initializeComponents
import com.pharmacy.core.crashlytics.CrashlyticsComponent
import com.pharmacy.core.logger.LoggerComponent
import com.pharmacy.core.theme.ThemeComponent
import com.pharmacy.di.DiComponent
import com.pharmacy.ui.activity.CrashActivity

class App : Application() {

    private var lastActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        initializeComponents(
            DiComponent,
            LoggerComponent,
            CrashlyticsComponent,
            ThemeComponent
        )

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityStarted(p0: Activity) {
                lastActivity = p0
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

        })

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            startActivity(CrashActivity.createIntent(this, throwable.stackTraceToString()))
            lastActivity?.finish()
        }
    }

}