package com.pharmacy.ui.screen.admin_reports_users.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pharmacy.R
import com.pharmacy.common.formatter.Formatters
import com.pharmacy.ui.screen.admin_reports_users.AdminReportsUsersViewModel
import com.pharmacy.ui.screen.admin_reports_users.model.mvi.AdminReportsUsersViewState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun AdminReportsUsersContent(viewModel: AdminReportsUsersViewModel) {
    val state by viewModel.collectAsState()
    AdminReportsUsersContent(
        state = state,
        onBackAction = viewModel::back
    )
}

@Composable
private fun AdminReportsUsersContent(
    state: AdminReportsUsersViewState,
    onBackAction: () -> Unit,
) {
    Scaffold(
        topBar = {
            AdminReportsUsersTopAppBar(
                onBackAction = onBackAction
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            if (state.isContentLoading) {
                AdminReportsUsersLoading()
            } else {
                AdminReportsUsersElements(
                    usersTotal = state.usersTotal,
                    activeUsers = state.activeUsers,
                    newUsersInWeek = state.newUsersInWeek,
                    ordersTotal = state.ordersTotal,
                    ordersInWeek = state.ordersInWeek,
                    averageOrderPrice = state.averageOrderPrice,
                )
            }
        }
    }
}

@Composable
private fun AdminReportsUsersTopAppBar(
    onBackAction: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackAction) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.content_profile_admin_reports_users_action_back)
                )
            }
        },
        title = {
            Text(text = stringResource(R.string.content_profile_admin_reports_users_title))
        }
    )
}

@Composable
private fun AdminReportsUsersLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun AdminReportsUsersElements(
    usersTotal: Int,
    activeUsers: Int,
    newUsersInWeek: Int,
    ordersTotal: Int,
    ordersInWeek: Int,
    averageOrderPrice: Double,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_users_total),
            value = Formatters.number.format(usersTotal),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_users_active_total),
            value = Formatters.number.format(activeUsers),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_users_new_in_week),
            value = Formatters.number.format(newUsersInWeek),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_users_orders_total),
            value = Formatters.number.format(ordersTotal),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_users_orders_new_in_week),
            value = Formatters.number.format(ordersInWeek),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_users_average_order_price),
            value = Formatters.currency.format(averageOrderPrice),
        )
    }
}

@Composable
private fun StatisticElement(
    title: String,
    value: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = value,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview
@Composable
private fun AdminReportsCategoriesContentPreview() {
    val state = AdminReportsUsersViewState(
        isContentLoading = false,
        usersTotal = 15_123,
        activeUsers = 560,
        newUsersInWeek = 56,
        ordersTotal = 1_245,
        ordersInWeek = 156,
        averageOrderPrice = 16_000.0,
    )
    AdminReportsUsersContent(
        state = state,
        onBackAction = {}
    )
}