package com.pharmacy.ui.screen.admin_reports_users.model.mvi

import android.os.Parcelable
import com.pharmacy.common.extensions.Zero
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminReportsUsersViewState(
    val isContentLoading: Boolean = true,
    val usersTotal: Int = Int.Zero,
    val activeUsers: Int = Int.Zero,
    val newUsersInWeek: Int = Int.Zero,
    val ordersTotal: Int = Int.Zero,
    val ordersInWeek: Int = Int.Zero,
    val averageOrderPrice: Double = Double.Zero,
) : Parcelable