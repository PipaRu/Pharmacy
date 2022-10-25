package com.pharmacy.core.crashlytics

import com.pharmacy.core.logger.Logger

class DefaultCrashlytics : Crashlytics {

    override fun record(throwable: Throwable, message: String?) {
        Logger.error(throwable = throwable, message = message)
    }

}