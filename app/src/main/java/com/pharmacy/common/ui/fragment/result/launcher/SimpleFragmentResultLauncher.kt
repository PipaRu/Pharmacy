package com.pharmacy.common.ui.fragment.result.launcher

sealed interface SimpleFragmentResultLauncher : FragmentResultRegistry {

    fun launch(key: String = defaultKey, savable: Boolean = true)

}