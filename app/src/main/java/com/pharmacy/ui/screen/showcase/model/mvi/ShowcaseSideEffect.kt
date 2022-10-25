package com.pharmacy.ui.screen.showcase.model.mvi

sealed class ShowcaseSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : ShowcaseSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : ShowcaseSideEffect()
}
