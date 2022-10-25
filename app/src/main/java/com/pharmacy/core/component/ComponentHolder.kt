package com.pharmacy.core.component

abstract class ComponentHolder<T> {

    private var _instance: T? = null

    protected val instance: T
        get() = requireNotNull(_instance) { "Instance not initialized" }

    fun setInstance(instance: T?) {
        _instance = instance
    }

}