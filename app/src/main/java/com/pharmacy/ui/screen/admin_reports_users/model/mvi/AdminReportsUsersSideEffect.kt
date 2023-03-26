package com.pharmacy.ui.screen.admin_reports_users.model.mvi

sealed class AdminReportsUsersSideEffect {
    object NavigateBack : AdminReportsUsersSideEffect()
}