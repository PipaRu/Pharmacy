package com.pharmacy.core.logger

import com.pharmacy.core.component.ComponentHolder

interface Logger {

    fun verbose(message: String? = null, throwable: Throwable? = null)
    fun error(message: String? = null, throwable: Throwable? = null)
    fun warning(message: String? = null, throwable: Throwable? = null)
    fun debug(message: String? = null, throwable: Throwable? = null)
    fun info(message: String? = null, throwable: Throwable? = null)

    companion object : ComponentHolder<Logger>(), Logger {

        override fun verbose(message: String?, throwable: Throwable?) = instance.verbose(message, throwable)

        override fun error(message: String?, throwable: Throwable?) = instance.error(message, throwable)

        override fun warning(message: String?, throwable: Throwable?) = instance.warning(message, throwable)

        override fun debug(message: String?, throwable: Throwable?) = instance.debug(message, throwable)

        override fun info(message: String?, throwable: Throwable?) = instance.info(message, throwable)

    }
}