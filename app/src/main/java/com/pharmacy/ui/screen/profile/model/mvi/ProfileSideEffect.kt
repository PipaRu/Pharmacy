package com.pharmacy.ui.screen.profile.model.mvi

sealed class ProfileSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : ProfileSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : ProfileSideEffect()
    object NavigateToOrders : ProfileSideEffect()
    object NavigateToAdministration : ProfileSideEffect()
    object SelectAddress : ProfileSideEffect()
    object DropNavigation : ProfileSideEffect()
}