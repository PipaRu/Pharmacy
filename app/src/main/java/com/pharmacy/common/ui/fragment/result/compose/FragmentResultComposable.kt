package com.pharmacy.common.ui.fragment.result.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.pharmacy.common.ui.fragment.result.FragmentResultController
import com.pharmacy.common.ui.fragment.result.FragmentResultDispatcher
import com.pharmacy.common.ui.fragment.result.FragmentResultHost
import com.pharmacy.common.ui.fragment.result.FragmentResultOwnerProvider

val LocalFragmentResultOwnerProvider = staticCompositionLocalOf { FragmentResultOwnerProvider.Empty }

@SuppressLint("ComposableNaming")
@Composable
fun <Value> FragmentResultHost<Value>.setResult(result: Value, key: String = defaultKey) {
    val fragmentResultProvider = LocalFragmentResultOwnerProvider.current
    FragmentResultController.setResult(
        this,
        fragmentResultProvider.fragmentResultOwner,
        key,
        result
    )
}

@SuppressLint("ComposableNaming")
@Composable
fun FragmentResultHost<Unit>.trigger(key: String = defaultKey) {
    setResult(result = Unit, key = key)
}

@Composable
fun <Value> FragmentResultHost<Value>.getDispatcher(): FragmentResultDispatcher<Value> {
    return FragmentResultDispatcher(
        defaultOwner = LocalFragmentResultOwnerProvider.current.fragmentResultOwner,
        defaultHost = this,
        defaultLifecycleOwner = LocalLifecycleOwner.current,
        defaultKey = defaultKey
    )
}