package com.pharmacy.ui.screen.admin_menu.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pharmacy.ui.screen.admin_menu.AdminMenuViewModel
import com.pharmacy.ui.screen.admin_menu.model.mvi.AdminMenuViewState
import org.orbitmvi.orbit.compose.collectAsState
import com.pharmacy.R.string.content_profile_admin_menu_action_back as action_back
import com.pharmacy.R.string.content_profile_admin_menu_action_products as action_products
import com.pharmacy.R.string.content_profile_admin_menu_action_reports as action_reports
import com.pharmacy.R.string.content_profile_admin_menu_title as title

@Composable
fun AdminMenuScreenContent(viewModel: AdminMenuViewModel) {
    val state: AdminMenuViewState by viewModel.collectAsState()
    AdminMenuContent(
        state = state,
        onBackAction = viewModel::back,
        onProductsAction = viewModel::products,
        onReportsAction = viewModel::reports
    )
}

@Composable
private fun AdminMenuContent(
    state: AdminMenuViewState,
    onBackAction: () -> Unit,
    onProductsAction: () -> Unit,
    onReportsAction: () -> Unit,
) {
    Scaffold(
        topBar = {
            AdminMenuTopAppBar(
                onBackAction = onBackAction
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            MenuContent(
                onProductsAction = onProductsAction,
                onReportsAction = onReportsAction
            )
        }
    }
}

@Composable
private fun AdminMenuTopAppBar(
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
private fun MenuContent(
    onProductsAction: () -> Unit,
    onReportsAction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SectionComponent(
            icon = Icons.Default.Inventory2,
            text = stringResource(action_products),
            onClick = onProductsAction
        )
        SectionComponent(
            icon = Icons.Default.Summarize,
            text = stringResource(action_reports),
            onClick = onReportsAction
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
    val state = AdminMenuViewState()
    AdminMenuContent(
        state = state,
        onBackAction = {},
        onProductsAction = {},
        onReportsAction = {}
    )
}