package com.pharmacy.common.ui.fragment.result

import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner

class FragmentResultDispatcher<Result>(
    private val defaultOwner: FragmentResultOwner,
    private val defaultHost: FragmentResultHost<Result>,
    private val defaultLifecycleOwner: LifecycleOwner,
    private val defaultKey: String
) {

    fun setResult(
        key: String = defaultKey,
        result: Result,
    ) {
        FragmentResultController.setResult(
            host = defaultHost,
            owner = defaultOwner,
            key = key,
            result = result
        )
    }

    fun setResultListener(
        lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
        key: String = defaultKey,
        listener: (Result) -> Unit
    ) {
        FragmentResultController.setResultListener(
            host = defaultHost,
            owner = defaultOwner,
            lifecycleOwner = lifecycleOwner,
            key = key,
            listener = listener,
        )
    }

    fun clearResult(key: String = defaultKey) {
        FragmentResultController.clearResult(
            host = defaultHost,
            owner = defaultOwner,
            key = key
        )
    }

    fun clearResultListener(key: String = defaultKey) {
        FragmentResultController.clearResultListener(
            host = defaultHost,
            owner = defaultOwner,
            key = key
        )
    }

    companion object {
        fun FragmentResultDispatcher<Unit>.trigger(key: String = defaultKey) {
            setResult(key, Unit)
        }
    }

}