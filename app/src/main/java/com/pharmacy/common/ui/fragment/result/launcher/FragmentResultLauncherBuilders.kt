package com.pharmacy.common.ui.fragment.result.launcher

import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.common.ui.fragment.result.FragmentResultController
import com.pharmacy.common.ui.fragment.result.FragmentResultHost

fun <Input> fragmentResultLauncher(
    savedStateRegistryOwnerProvider: () -> SavedStateRegistryOwner,
    defaultKey: String,
    subscribe: FragmentResultRegistry.(key: String) -> Unit,
    unsubscribe: FragmentResultRegistry.(key: String) -> Unit,
    release: FragmentResultRegistry.(key: String) -> Unit,
    launch: FragmentResultRegistry.(key: String, Input) -> Unit
): FragmentResultLauncher<Input> {
    return DefaultFragmentResultLauncher(
        savedStateRegistryOwnerProvider = savedStateRegistryOwnerProvider,
        defaultKey = defaultKey,
        subscribe = subscribe,
        unsubscribe = unsubscribe,
        release = release,
        launch = launch
    )
}

fun <Input, Value> fragmentResultLauncher(
    host: FragmentResultHost<Value>,
    owner: () -> FragmentResultOwner,
    savedStateRegistryOwner: () -> SavedStateRegistryOwner,
    lifecycleOwner: () -> LifecycleOwner,
    launch: FragmentResultRegistry.(key: String, Input) -> Unit,
    listener: (Value) -> Unit,
): FragmentResultLauncher<Input> {
    return DefaultFragmentResultLauncher<Input>(
        savedStateRegistryOwnerProvider = savedStateRegistryOwner,
        defaultKey = host.defaultKey,
        subscribe = { key ->
            FragmentResultController.setResultListener(
                host = host,
                owner = owner.invoke(),
                lifecycleOwner = lifecycleOwner.invoke(),
                key = key,
                listener = { value ->
                    listener.invoke(value)
                    unsubscribe(key)
                }
            )
        },
        unsubscribe = { key ->
            FragmentResultController.clearResultListener(
                host = host,
                owner = owner.invoke(),
                key = key
            )
        },
        release = { key ->
            FragmentResultController.clearResult(
                host = host,
                owner = owner.invoke(),
                key = key
            )
        },
        launch = launch
    )
}

fun <Input, Value> AppFragment.registerForFragmentResult(
    host: FragmentResultHost<Value>,
    owner: () -> FragmentResultOwner = { fragmentResultOwner },
    savedStateRegistryOwner: () -> SavedStateRegistryOwner = { this },
    lifecycleOwner: () -> LifecycleOwner = { this },
    launch: FragmentResultRegistry.(key: String, Input) -> Unit,
    listener: (Value) -> Unit,
): FragmentResultLauncher<Input> = fragmentResultLauncher(
    host = host, owner = owner,
    savedStateRegistryOwner = savedStateRegistryOwner,
    lifecycleOwner = lifecycleOwner,
    launch = launch,
    listener = listener
)