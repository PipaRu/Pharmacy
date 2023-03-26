package com.pharmacy.ui.screen.admin_reports_menu.model.mvi

sealed class AdminReportsMenuSideEffect {
    data class ShowContentInDeveloping(val contentName: String? = null) : AdminReportsMenuSideEffect()
    data class ShowSomethingWentWrong(val target: String? = null) : AdminReportsMenuSideEffect()
    object NavigateBack : AdminReportsMenuSideEffect()
    object NavigateUsersReport : AdminReportsMenuSideEffect()
    object NavigateCategoriesReport : AdminReportsMenuSideEffect()
    object NavigateProductsReport : AdminReportsMenuSideEffect()
}