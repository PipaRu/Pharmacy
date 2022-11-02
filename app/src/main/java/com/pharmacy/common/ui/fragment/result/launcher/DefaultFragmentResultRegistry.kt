package com.pharmacy.common.ui.fragment.result.launcher

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.pharmacy.common.extensions.addObserver

class DefaultFragmentResultRegistry(
    override val defaultKey: String,
    private val subscribe: FragmentResultRegistry.(key: String) -> Unit,
    private val unsubscribe: FragmentResultRegistry.(key: String) -> Unit,
    private val release: FragmentResultRegistry.(key: String) -> Unit,
    savedStateRegistryOwnerProvider: () -> SavedStateRegistryOwner,
) : FragmentResultRegistry {

    private companion object {
        private const val KEY_SAVED_STATE = "listeners_state"
        private const val KEY_SAVED_LISTENER_KEYS = "keys"
    }

    private val savedStateKey: String
        get() = "${KEY_SAVED_STATE}_$defaultKey"

    private val activeListeners = mutableSetOf<String>()

    init {
        val savedStateRegistryOwner = savedStateRegistryOwnerProvider.invoke()
        val lifecycle = savedStateRegistryOwner.lifecycle
        val savedStateRegistry = savedStateRegistryOwner.savedStateRegistry
        lifecycle.addObserver { event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                initializeSaveStateListeners(savedStateRegistry)
            }
        }
    }

    override fun subscribe(key: String, savable: Boolean) {
        if (savable) activeListeners.add(key)
        subscribe.invoke(this, key)
    }

    override fun unsubscribe(key: String) {
        activeListeners.remove(key)
        unsubscribe.invoke(this, key)
    }

    override fun release(key: String) {
        activeListeners.remove(key)
        release.invoke(this, key)
    }

    private fun initializeSaveStateListeners(savedStateRegistry: SavedStateRegistry) {
        savedStateRegistry
            .consumeRestoredStateForKey(savedStateKey)
            ?.getStringArrayList(KEY_SAVED_LISTENER_KEYS)
            .orEmpty()
            .forEach { key -> subscribe(key = key, savable = true) }

        if (savedStateRegistry.getSavedStateProvider(savedStateKey) == null) {
            savedStateRegistry.registerSavedStateProvider(savedStateKey) {
                Bundle(1).apply {
                    putStringArrayList(KEY_SAVED_LISTENER_KEYS, ArrayList(activeListeners))
                }
            }
        }
    }

}