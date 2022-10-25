package com.pharmacy.core.logger

import android.app.Application
import com.pharmacy.core.component.Component

object LoggerComponent : Component {
    override fun init(application: Application) {
        Logger.setInstance(TimberLogger())
    }
}