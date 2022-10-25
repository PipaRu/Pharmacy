package com.pharmacy.common.ui.fragment.result

import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner

object FragmentResultController {

    fun <Result> setResult(
        host: FragmentResultHost<Result>,
        owner: FragmentResultOwner,
        key: String = host.defaultKey,
        result: Result
    ) {
        owner.setFragmentResult(key, host.packer.boxing(result))
    }

    fun <Result> setResultListener(
        host: FragmentResultHost<Result>,
        owner: FragmentResultOwner,
        lifecycleOwner: LifecycleOwner,
        key: String = host.defaultKey,
        listener: (Result) -> Unit
    ) {
        owner.setFragmentResultListener(key, lifecycleOwner) { _, data ->
            listener.invoke(host.packer.unboxing(data))
        }
    }

    fun <Result> clearResult(
        host: FragmentResultHost<Result>,
        owner: FragmentResultOwner,
        key: String = host.defaultKey
    ) {
        owner.clearFragmentResult(key)
    }

    fun <Result> clearResultListener(
        host: FragmentResultHost<Result>,
        owner: FragmentResultOwner,
        key: String = host.defaultKey
    ) {
        owner.clearFragmentResultListener(key)
    }

}