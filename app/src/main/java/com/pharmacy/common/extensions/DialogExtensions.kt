package com.pharmacy.common.extensions

import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun Dialog.showWithLifecycle(lifecycleOwner: LifecycleOwner) {
    val lifecycle = lifecycleOwner.lifecycle
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) return
    lifecycle.addObserver { event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> show()
            Lifecycle.Event.ON_DESTROY -> dismiss()
            else -> Unit
        }
    }
}