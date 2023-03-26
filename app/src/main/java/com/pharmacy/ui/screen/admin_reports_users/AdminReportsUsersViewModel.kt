package com.pharmacy.ui.screen.admin_reports_users

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pharmacy.common.mock.MockDelay
import com.pharmacy.core.crashlytics.Crashlytics
import com.pharmacy.ui.screen.admin_reports_users.model.mvi.AdminReportsUsersSideEffect
import com.pharmacy.ui.screen.admin_reports_users.model.mvi.AdminReportsUsersViewState
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import kotlin.random.Random

class AdminReportsUsersViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<AdminReportsUsersViewState, AdminReportsUsersSideEffect> {

    private val mockAdminReportsUsersViewState = AdminReportsUsersViewState(
        isContentLoading = false,
        usersTotal = (1_000..20_000).random(),
        activeUsers = (500..10_000).random(),
        newUsersInWeek = (10..500).random(),
        ordersTotal = (1_000..10_000).random(),
        ordersInWeek = (100..500).random(),
        averageOrderPrice = Random.nextDouble(600.0, 10_000.0),
    )

    override val container = container<AdminReportsUsersViewState, AdminReportsUsersSideEffect>(
        initialState = AdminReportsUsersViewState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(
            exceptionHandler = Crashlytics.coroutineExceptionHandler,
        ),
    )

    init {
        intent {
            repeatOnSubscription {
                delay(MockDelay.MEDIUM)
                reduce { mockAdminReportsUsersViewState }
            }
        }
    }

    fun back() = intent { postSideEffect(AdminReportsUsersSideEffect.NavigateBack) }

}