package com.pharmacy.common.ui.fragment.result.launcher

import androidx.savedstate.SavedStateRegistryOwner

class DefaultFragmentResultLauncher<I>(
    savedStateRegistryOwnerProvider: () -> SavedStateRegistryOwner,
    defaultKey: String,
    subscribe: FragmentResultRegistry.(key: String) -> Unit,
    unsubscribe: FragmentResultRegistry.(key: String) -> Unit,
    release: FragmentResultRegistry.(key: String) -> Unit,
    private val launch: FragmentResultRegistry.(key: String, I) -> Unit
) : FragmentResultLauncher<I> {

    private val registry: FragmentResultRegistry = DefaultFragmentResultRegistry(
        defaultKey = defaultKey,
        subscribe = subscribe,
        unsubscribe = unsubscribe,
        release = release,
        savedStateRegistryOwnerProvider = savedStateRegistryOwnerProvider
    )

    override val defaultKey: String get() = registry.defaultKey

    override fun subscribe(key: String, savable: Boolean) = registry.subscribe(key, savable)

    override fun unsubscribe(key: String) = registry.unsubscribe(key)

    override fun release(key: String) = registry.release(key)

    override fun launch(key: String, input: I, savable: Boolean) = launch.invoke(this, key, input)

}