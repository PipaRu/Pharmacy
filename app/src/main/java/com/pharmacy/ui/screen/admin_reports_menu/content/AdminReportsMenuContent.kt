package com.pharmacy.ui.screen.admin_reports_menu.content

import androidx.compose.runtime.Composable
import com.pharmacy.ui.screen.admin_reports_menu.AdminReportsMenuViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pharmacy.ui.screen.admin_reports_menu.model.mvi.AdminReportsMenuViewState
import org.orbitmvi.orbit.compose.collectAsState
import com.pharmacy.R.string.content_profile_admin_reports_menu_action_back as action_back
import com.pharmacy.R.string.content_profile_admin_reports_menu_action_users as action_users
import com.pharmacy.R.string.content_profile_admin_reports_menu_action_product_categories as action_categories
import com.pharmacy.R.string.content_profile_admin_reports_menu_action_products as action_products
import com.pharmacy.R.string.content_profile_admin_reports_menu_title as title

@Composable
fun AdminReportsMenuContent(viewModel: AdminReportsMenuViewModel) {
    val state: AdminReportsMenuViewState by viewModel.collectAsState()
    AdminReportsMenuContent(
        state = state,
        onBackAction = viewModel::back,
        onUsersAction = viewModel::usersReport,
        onCategoriesAction = viewModel::categoriesReport,
        onProductAction = viewModel::productsReport,
    )
}

@Composable
private fun AdminReportsMenuContent(
    state: AdminReportsMenuViewState,
    onBackAction: () -> Unit,
    onUsersAction: () -> Unit,
    onCategoriesAction: () -> Unit,
    onProductAction: () -> Unit,
) {
    Scaffold(
        topBar = {
            AdminReportsMenuTopAppBar(
                onBackAction = onBackAction
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            AdminReportsMenuElements(
                onUsersAction = onUsersAction,
                onCategoriesAction = onCategoriesAction,
                onProductsAction = onProductAction,
            )
        }
    }
}

@Composable
private fun AdminReportsMenuTopAppBar(
    onBackAction: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackAction) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(action_back)
                )
            }
        },
        title = {
            Text(text = stringResource(title))
        }
    )
}

@Composable
private fun AdminReportsMenuElements(
    onUsersAction: () -> Unit,
    onCategoriesAction: () -> Unit,
    onProductsAction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SectionComponent(
            icon = Icons.Default.Person,
            text = stringResource(action_users),
            onClick = onUsersAction
        )
        SectionComponent(
            icon = Icons.Default.Category,
            text = stringResource(action_categories),
            onClick = onCategoriesAction
        )
        SectionComponent(
            icon = Icons.Default.Inventory2,
            text = stringResource(action_products),
            onClick = onProductsAction
        )
    }
}

@Composable
private fun SectionComponent(
    icon: ImageVector,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        onClick = onClick,
        enabled = enabled
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterVertically),
                imageVector = icon,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = text,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview
@Composable
private fun AdminMenuScreenPreview() {
    val state = AdminReportsMenuViewState()
    AdminReportsMenuContent(
        state = state,
        onBackAction = {},
        onUsersAction = {},
        onCategoriesAction = {},
        onProductAction = {},
    )
}