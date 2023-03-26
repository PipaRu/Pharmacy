package com.pharmacy.ui.screen.admin_reports_menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.ui.screen.admin_reports_menu.model.mvi.AdminReportsMenuSideEffect
import com.pharmacy.ui.screen.admin_reports_menu.model.mvi.AdminReportsMenuViewState
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

class AdminReportsMenuViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AdminReportsMenuViewState, AdminReportsMenuSideEffect> {

    override val container = container<AdminReportsMenuViewState, AdminReportsMenuSideEffect>(
        initialState = AdminReportsMenuViewState(),
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler,
        ),
        savedStateHandle = savedStateHandle,
    )

    fun back() = intent { postSideEffect(AdminReportsMenuSideEffect.NavigateBack) }

    fun usersReport() = intent { postSideEffect(AdminReportsMenuSideEffect.NavigateUsersReport) }

    fun categoriesReport() = intent { postSideEffect(AdminReportsMenuSideEffect.NavigateCategoriesReport) }

    fun productsReport() = intent { postSideEffect(AdminReportsMenuSideEffect.NavigateProductsReport) }

}