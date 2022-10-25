package com.pharmacy.common.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver

fun Lifecycle.addObserver(block: Lifecycle.(Lifecycle.Event) -> Unit): LifecycleObserver {
    val observer: LifecycleObserver = LifecycleEventObserver { _, event ->
        block.invoke(this, event)
    }
    addObserver(observer)
    return observer
}