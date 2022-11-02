package com.pharmacy.common.ui.fragment.result.launcher

import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.common.ui.fragment.result.FragmentResultController
import com.pharmacy.common.ui.fragment.result.FragmentResultHost

fun fragmentResultRegistry(
    defaultKey: String,
    subscribe: FragmentResultRegistry.(String) -> Unit,
    unsubscribe: FragmentResultRegistry.(String) -> Unit,
    release: FragmentResultRegistry.(key: String) -> Unit,
    savedStateRegistryOwnerProvider: () -> SavedStateRegistryOwner
): FragmentResultRegistry {
    return DefaultFragmentResultRegistry(
        defaultKey = defaultKey,
        subscribe = subscribe,
        unsubscribe = unsubscribe,
        release = release,
        savedStateRegistryOwnerProvider = savedStateRegistryOwnerProvider
    )
}

fun <Value> fragmentResultRegistry(
    fragmentResult: FragmentResultHost<Value>,
    fragmentResultOwnerProvider: () -> FragmentResultOwner,
    savedStateRegistryOwnerProvider: () -> SavedStateRegistryOwner,
    lifecycleOwnerProvider: () -> LifecycleOwner,
    listener: (Value) -> Unit
): FragmentResultRegistry = fragmentResultRegistry(
    defaultKey = fragmentResult.defaultKey,
    subscribe = { key ->
        FragmentResultController.setResultListener(
            host = fragmentResult,
            owner = fragmentResultOwnerProvider.invoke(),
            lifecycleOwner = lifecycleOwnerProvider.invoke(),
            key = key,
            listener = listener
        )
    },
    unsubscribe = { key ->
        FragmentResultController.clearResultListener(
            host = fragmentResult,
            owner = fragmentResultOwnerProvider.invoke(),
            key = key
        )
    },
    release = { key ->
        FragmentResultController.clearResult(
            host = fragmentResult,
            owner = fragmentResultOwnerProvider.invoke(),
            key = key
        )
    },
    savedStateRegistryOwnerProvider = savedStateRegistryOwnerProvider,
)

fun <Value> AppFragment.registerFragmentResultRegistry(
    fragmentResult: FragmentResultHost<Value>,
    fragmentResultOwnerProvider: () -> FragmentResultOwner = { fragmentResultOwner },
    savedStateRegistryOwnerProvider: () -> SavedStateRegistryOwner = { this },
    lifecycleOwnerProvider: () -> LifecycleOwner = { this },
    listener: (Value) -> Unit
): FragmentResultRegistry = fragmentResultRegistry(
    fragmentResult = fragmentResult,
    fragmentResultOwnerProvider = fragmentResultOwnerProvider,
    savedStateRegistryOwnerProvider = savedStateRegistryOwnerProvider,
    lifecycleOwnerProvider = lifecycleOwnerProvider,
    listener = listener
)