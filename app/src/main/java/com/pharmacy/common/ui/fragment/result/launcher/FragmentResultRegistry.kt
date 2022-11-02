package com.pharmacy.common.ui.fragment.result.launcher

interface FragmentResultRegistry {

    val defaultKey: String

    fun subscribe(key: String = defaultKey, savable: Boolean = true)

    fun unsubscribe(key: String = defaultKey)

    fun release(key: String = defaultKey)

}