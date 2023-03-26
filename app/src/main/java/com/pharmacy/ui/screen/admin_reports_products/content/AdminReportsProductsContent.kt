@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.screen.admin_reports_products.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pharmacy.R
import com.pharmacy.common.formatter.Formatters
import com.pharmacy.core.logger.Logger
import com.pharmacy.ui.model.*
import com.pharmacy.ui.screen.admin_reports_products.AdminReportsProductsViewModel
import com.pharmacy.ui.screen.admin_reports_products.model.mvi.AdminReportsProductsViewState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun AdminReportsProductsContent(viewModel: AdminReportsProductsViewModel) {
    val state: AdminReportsProductsViewState by viewModel.collectAsState()
    AdminReportsProductsContent(
        state = state,
        onProductSelected = viewModel::selectProduct,
        onBackAction = viewModel::back,
        onChangeQuery = viewModel::changeQuery,
        onSearch = viewModel::search,
    )
}

@Composable
private fun AdminReportsProductsContent(
    state: AdminReportsProductsViewState,
    onProductSelected: (ProductItem) -> Unit,
    onBackAction: () -> Unit,
    onChangeQuery: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                state = state,
                onBackAction = onBackAction
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            MainContent(
                state = state,
                onProductSelected = onProductSelected,
                onChangeQuery = onChangeQuery,
                onSearch = onSearch,
            )
        }
    }
}

@Composable
private fun TopAppBar(
    state: AdminReportsProductsViewState,
    onBackAction: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackAction) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.content_profile_admin_reports_products_action_back)
                )
            }
        },
        title = {
            Text(
                text = if (state.selectedProductStatistic is Resource.Data) {
                    state.selectedProductStatistic.value.product.name
                } else {
                    stringResource(R.string.content_profile_admin_reports_products_title)
                }
            )
        }
    )
}

@Composable
private fun MainContent(
    state: AdminReportsProductsViewState,
    onProductSelected: (ProductItem) -> Unit,
    onChangeQuery: (String) -> Unit,
    onSearch: () -> Unit,
) {
    val (isInitialLoading, query, products, selectedProductStatistic) = state
    Logger.error("MainContent $state")
    when {
        isInitialLoading -> LoadingContent()
        selectedProductStatistic is Resource.Loading -> LoadingContent()
        selectedProductStatistic is Resource.Data -> SelectedProductContent(
            productStatistic = selectedProductStatistic.value,
        )
        else -> AllProductsContent(
            query = query,
            products = products,
            onQueryChange = onChangeQuery,
            onSearch = onSearch,
            onProductSelected = onProductSelected,
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun AllProductsContent(
    query: String?,
    products: Resource<ParcelableList<ProductItem>>,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onProductSelected: (ProductItem) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            value = query.orEmpty(),
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = LocalTextStyle.current.copy(),
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch.invoke() }),
        )
        if (products is Resource.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (products is Resource.Data) {
            ProductsList(
                values = products.value,
                onProductSelected = onProductSelected,
            )
        }
    }
}

@Composable
private fun ProductsList(
    values: List<ProductItem>,
    onProductSelected: (ProductItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(values) { category ->
            ProductComponent(
                product = category,
                onClick = { onProductSelected.invoke(category) },
            )
        }
    }
}

@Composable
private fun ProductComponent(
    product: ProductItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        onClick = onClick,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                text = product.name,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}



@Composable
private fun SelectedProductContent(
    productStatistic: ProductStatistic,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_products_statistic_orders_count),
            value = Formatters.number.format(productStatistic.totalOrdersCount),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_products_statistic_orders_count_in_week),
            value = Formatters.number.format(productStatistic.ordersInWeek),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_products_statistic_totla_profit),
            value = Formatters.currency.format(productStatistic.totalProfit),
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
private fun AdminReportsProductsContentPreview() {
    val state = AdminReportsProductsViewState(
        query = null,
        products = Resource.Data(
            value = List(10) {
                ProductItem.Empty
            }.toParcelableList()
        ),
        selectedProductStatistic = Resource.Data(
            value = ProductStatistic(
                product = ProductItem.Empty,
                totalOrdersCount = 11,
                ordersInWeek = 13451,
                totalProfit = 1_214_125.0,
            )
        ),
    )
    AdminReportsProductsContent(
        state = state.copy(selectedProductStatistic = Resource.Nothing),
        onProductSelected = {},
        onBackAction = {},
        onChangeQuery = {},
        onSearch = {}
    )
}