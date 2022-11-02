package com.pharmacy.common.ui.fragment.result.launcher

sealed interface FragmentResultLauncher<I> : FragmentResultRegistry {

    fun launch(key: String = defaultKey, input: I, savable: Boolean = true)

}