package com.pharmacy.common.ui.fragment.result.launcher

import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.pharmacy.common.ui.fragment.AppFragment
import com.pharmacy.common.ui.fragment.result.FragmentResultController
import com.pharmacy.common.ui.fragment.result.FragmentResultHost

fun fragmentResultLauncher(
    savedStateRegistryOwnerProvider: () -> SavedStateRegistryOwner,
    defaultKey: String,
    subscribe: FragmentResultRegistry.(key: String) -> Unit,
    unsubscribe: FragmentResultRegistry.(key: String) -> Unit,
    release: FragmentResultRegistry.(key: String) -> Unit,
    launch: FragmentResultRegistry.(key: String) -> Unit
): SimpleFragmentResultLauncher {
    return DefaultSimpleFragmentResultLauncher(
        savedStateRegistryOwnerProvider = savedStateRegistryOwnerProvider,
        defaultKey = defaultKey,
        subscribe = subscribe,
        unsubscribe = unsubscribe,
        release = release,
        launch = { key, _ -> launch.invoke(this, key) }
    )
}

fun <Value> fragmentResultLauncher(
    fragmentResult: FragmentResultHost<Value>,
    fragmentResultOwner: () -> FragmentResultOwner,
    savedStateRegistryOwner: () -> SavedStateRegistryOwner,
    lifecycleOwner: () -> LifecycleOwner,
    launch: (key: String) -> Unit,
    listener: (Value) -> Unit,
): SimpleFragmentResultLauncher {
    return DefaultSimpleFragmentResultLauncher(
        savedStateRegistryOwnerProvider = savedStateRegistryOwner,
        defaultKey = fragmentResult.defaultKey,
        subscribe = { key ->
            FragmentResultController.setResultListener(
                host = fragmentResult,
                owner = fragmentResultOwner.invoke(),
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
                host = fragmentResult,
                owner = fragmentResultOwner.invoke(),
                key = key
            )
        },
        release = { key ->
            FragmentResultController.clearResult(
                host = fragmentResult,
                owner = fragmentResultOwner.invoke(),
                key = key
            )
        },
        launch = { key, savable ->
            subscribe(key, savable)
            launch.invoke(key)
        }
    )
}

fun <Value> AppFragment.registerForFragmentResult(
    fragmentResult: FragmentResultHost<Value>,
    owner: () -> FragmentResultOwner = { fragmentResultOwner },
    savedStateRegistryOwner: () -> SavedStateRegistryOwner = { this },
    lifecycleOwner: () -> LifecycleOwner = { this },
    launch: (key: String) -> Unit,
    listener: (Value) -> Unit,
): SimpleFragmentResultLauncher = fragmentResultLauncher(
    fragmentResult = fragmentResult,
    fragmentResultOwner = owner,
    savedStateRegistryOwner = savedStateRegistryOwner,
    lifecycleOwner = lifecycleOwner,
    launch = launch,
    listener = listener
)