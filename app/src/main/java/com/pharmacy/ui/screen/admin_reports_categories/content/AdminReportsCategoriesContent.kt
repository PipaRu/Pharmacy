@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.screen.admin_reports_categories.content

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
import com.pharmacy.ui.model.*
import com.pharmacy.ui.screen.admin_reports_categories.AdminReportsCategoriesViewModel
import com.pharmacy.ui.screen.admin_reports_categories.model.mvi.AdminReportsCategoriesViewState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun AdminReportsCategoriesContent(viewModel: AdminReportsCategoriesViewModel) {
    val state by viewModel.collectAsState()
    AdminReportsCategoriesScreenContent(
        state = state,
        onCategorySelected = viewModel::selectCategory,
        onBackAction = viewModel::back,
        onChangeQuery = viewModel::changeQuery,
        onSearch = viewModel::search,
    )
}

@Composable
private fun AdminReportsCategoriesScreenContent(
    state: AdminReportsCategoriesViewState,
    onCategorySelected: (CategoryItem) -> Unit,
    onBackAction: () -> Unit,
    onChangeQuery: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Scaffold(
        topBar = {
            AdminReportsCategoriesTopAppBar(
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
            AdminReportsCategoriesContent(
                state = state,
                onCategorySelected = onCategorySelected,
                onChangeQuery = onChangeQuery,
                onSearch = onSearch,
            )
        }
    }

}


@Composable
private fun AdminReportsCategoriesTopAppBar(
    state: AdminReportsCategoriesViewState,
    onBackAction: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackAction) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.content_profile_admin_reports_categories_action_back)
                )
            }
        },
        title = {
            Text(
                text = if (state.selectedCategoryStatistic is Resource.Data) {
                    state.selectedCategoryStatistic.value.category.name
                } else {
                    stringResource(R.string.content_profile_admin_reports_categories_title)
                }
            )
        }
    )
}

@Composable
private fun AdminReportsCategoriesContent(
    state: AdminReportsCategoriesViewState,
    onChangeQuery: (String) -> Unit,
    onSearch: () -> Unit,
    onCategorySelected: (CategoryItem) -> Unit,
) {
    val (isInitialLoading, query, categories, selectedCategoryStatistic) = state
    when {
        isInitialLoading -> LoadingContent()
        selectedCategoryStatistic is Resource.Loading -> LoadingContent()
        selectedCategoryStatistic is Resource.Data -> AdminReportsCategoriesSelectedContent(
            categoryStatistic = selectedCategoryStatistic.value,
        )
        else -> AllCategoriesContent(
            query = query,
            categories = categories,
            onQueryChange = onChangeQuery,
            onSearch = onSearch,
            onCategorySelected = onCategorySelected,
        )
    }
}


@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun AllCategoriesContent(
    query: String?,
    categories: Resource<ParcelableList<CategoryItem>>,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onCategorySelected: (CategoryItem) -> Unit,
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
            placeholder = { Text(text = stringResource(R.string.search),) },
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
        if (categories is Resource.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (categories is Resource.Data) {
            CategoriesList(
                values = categories.value,
                onCategorySelected = onCategorySelected,
            )
        }
    }
}

@Composable
private fun CategoriesList(
    values: List<CategoryItem>,
    onCategorySelected: (CategoryItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(values) { category ->
            CategoryComponent(
                category = category,
                onClick = { onCategorySelected.invoke(category) },
            )
        }
    }
}

@Composable
private fun CategoryComponent(
    category: CategoryItem,
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
                text = category.name,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}


@Composable
private fun AdminReportsCategoriesSelectedContent(
    categoryStatistic: CategoryStatistic,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_categories_statistic_product_count),
            value = Formatters.number.format(categoryStatistic.productsCount),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_categories_statistic_orders_count),
            value = Formatters.number.format(categoryStatistic.totalOrdersCount),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_categories_statistic_orders_count_in_week),
            value = Formatters.number.format(categoryStatistic.ordersInWeek),
        )
        StatisticElement(
            title = stringResource(R.string.content_profile_admin_reports_categories_statistic_profit),
            value = Formatters.currency.format(categoryStatistic.totalProfit),
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
    val state = AdminReportsCategoriesViewState(
        isInitialLoading = false,
        query = "QQ",
        categories = Resource.Data(
            value = List(10) {
                CategoryItem("Category #$it")
            }.toParcelableList()
        ),
        selectedCategoryStatistic = Resource.Data(
            value = CategoryStatistic(
                category = CategoryItem("Categ"),
                productsCount = 10,
                totalOrdersCount = 11,
                ordersInWeek = 13451,
                totalProfit = 1_214_125.0,
            )
        ),
    )
    AdminReportsCategoriesScreenContent(
        state = state.copy(selectedCategoryStatistic = Resource.Nothing),
        onCategorySelected = {},
        onBackAction = {},
        onChangeQuery = {},
        onSearch = {}
    )
}