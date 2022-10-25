package com.pharmacy.core.crashlytics

import com.pharmacy.core.component.ComponentHolder
import kotlinx.coroutines.CoroutineExceptionHandler

interface Crashlytics {

    fun record(throwable: Throwable, message: String? = null)

    companion object : ComponentHolder<Crashlytics>(), Crashlytics {

        val coroutineExceptionHandler = coroutineExceptionHandler()

        fun coroutineExceptionHandler(block: ((error: Throwable) -> Unit)? = null): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { _, error ->
                record(error)
                block?.invoke(error)
            }
        }

        override fun record(throwable: Throwable, message: String?) = instance.record(throwable, message)

    }

}