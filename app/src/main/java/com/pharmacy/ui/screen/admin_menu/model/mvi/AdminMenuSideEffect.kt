package com.pharmacy.ui.screen.admin_menu.model.mvi

sealed class AdminMenuSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : AdminMenuSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : AdminMenuSideEffect()
    object NavigateToProducts : AdminMenuSideEffect()
    object NavigateBack : AdminMenuSideEffect()
}