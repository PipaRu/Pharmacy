package com.pharmacy.ui.screen.admin_menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.ui.screen.admin_menu.model.mvi.AdminMenuSideEffect
import com.pharmacy.ui.screen.admin_menu.model.mvi.AdminMenuViewState
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

class AdminMenuViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AdminMenuViewState, AdminMenuSideEffect> {

    override val container = container<AdminMenuViewState, AdminMenuSideEffect>(
        initialState = AdminMenuViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler {
                intent { postSideEffect(AdminMenuSideEffect.ShowSomethingWentWrong()) }
            }
        )
    )

    fun back() = intent {
        postSideEffect(AdminMenuSideEffect.NavigateBack)
    }

    fun products() = intent {
        postSideEffect(AdminMenuSideEffect.NavigateToProducts)
    }

    fun reports() = intent {
        postSideEffect(AdminMenuSideEffect.NavigateToProducts)
    }

}