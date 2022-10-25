@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.screen.basket.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pharmacy.common.formatter.Formatters
import com.pharmacy.common.ui.compose.layout.plus
import com.pharmacy.common.ui.compose.text.field.OutlinedTextField
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.SelectableItem
import com.pharmacy.ui.screen.basket.BasketViewModel
import com.pharmacy.ui.screen.basket.model.mvi.BasketViewState
import org.orbitmvi.orbit.compose.collectAsState
import com.pharmacy.R.string.content_basket_action_buy as action_buy
import com.pharmacy.R.string.content_basket_action_buy_empty as action_buy_empty
import com.pharmacy.R.string.content_basket_message_empty as message_empty
import com.pharmacy.R.string.content_basket_search_placeholder as search_placeholder

@Composable
fun BasketScreenContent(viewModel: BasketViewModel) {
    val state by viewModel.collectAsState()
    BasketContent(
        state = state,
        onQueryChanged = viewModel::query,
        onSearchAction = viewModel::search,
        onOpenFiltersAction = viewModel::openFilters,
        onProductClicked = viewModel::onProductClicked,
        onProductSelected = viewModel::onProductSelected,
        onDeleteAllSelectedAction = viewModel::deleteAllSelected,
        onBuyAllSelectedAction = viewModel::tryBuyAllSelected,
        onAdd = viewModel::addProduct,
        onRemove = viewModel::removeProduct
    )
}

@Composable
private fun BasketContent(
    state: BasketViewState,
    onQueryChanged: (String) -> Unit,
    onSearchAction: () -> Unit,
    onOpenFiltersAction: () -> Unit,
    onProductClicked: (BasketBunchItem) -> Unit,
    onProductSelected: (BasketBunchItem, Boolean) -> Unit,
    onDeleteAllSelectedAction: () -> Unit,
    onBuyAllSelectedAction: () -> Unit,
    onAdd: (BasketBunchItem) -> Unit,
    onRemove: (BasketBunchItem) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBarContent(
                query = state.query,
                onQueryChanged = onQueryChanged,
                onSearchAction = onSearchAction,
                onOpenFiltersAction = onOpenFiltersAction
            )
        },
        content = { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings + PaddingValues(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isProductsLoading -> {
                        BasketLoadingContent()
                    }
                    state.items.isEmpty() -> {
                        BasketEmptyContent()
                    }
                    else -> {
                        AllProductsContent(
                            products = state.items,
                            onProductClicked = onProductClicked,
                            onProductSelected = onProductSelected,
                            onDeleteAllSelectedAction = onDeleteAllSelectedAction,
                            onBuyAllSelectedAction = onBuyAllSelectedAction,
                            onAdd = onAdd,
                            onRemove = onRemove
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun TopAppBarContent(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchAction: () -> Unit,
    onOpenFiltersAction: () -> Unit,
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                modifier = Modifier.height(36.dp),
                value = query,
                onValueChange = onQueryChanged,
                placeholder = { Text(text = stringResource(search_placeholder)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                maxLines = 1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.onSurface,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.surface
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchAction.invoke() }),
                contentPaddings = PaddingValues(4.dp)
            )
        },
        actions = {
            IconButton(onClick = onOpenFiltersAction) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun BasketLoadingContent() {
    CircularProgressIndicator()
}

@Composable
private fun BasketEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = message_empty),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AllProductsContent(
    products: List<SelectableItem<BasketBunchItem>>,
    onProductClicked: (BasketBunchItem) -> Unit,
    onProductSelected: (BasketBunchItem, Boolean) -> Unit,
    onDeleteAllSelectedAction: () -> Unit,
    onBuyAllSelectedAction: () -> Unit,
    onAdd: (BasketBunchItem) -> Unit,
    onRemove: (BasketBunchItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(
                items = products,
                key = { element -> element.value.id },
                itemContent = { element ->
                    BasketProductElement(
                        product = element.value,
                        isSelected = element.isSelected,
                        onClick = { onProductClicked.invoke(element.value) },
                        onSelected = { isSelected ->
                            onProductSelected.invoke(element.value, isSelected)
                        },
                        onAdd = { onAdd.invoke(element.value) },
                        onRemove = { onRemove.invoke(element.value) }
                    )
                }
            )
        }
        BasketActionComponent(
            products = products,
            onDeleteAllSelectedAction = onDeleteAllSelectedAction,
            onBuyAllSelectedAction = onBuyAllSelectedAction
        )
    }
}

@Composable
private fun BasketActionComponent(
    products: List<SelectableItem<BasketBunchItem>>,
    onDeleteAllSelectedAction: () -> Unit,
    onBuyAllSelectedAction: () -> Unit,
) {
    val isAnySelected by derivedStateOf { products.any { element -> element.isSelected } }
    val selected by derivedStateOf {
        products
            .filter { element -> element.isSelected }
            .map { element -> element.value }
    }
    val sumFinal by derivedStateOf { selected.sumOf { element -> element.product.price.finalPrice } }
    val sumStart by derivedStateOf { selected.sumOf { element -> element.product.price.startPrice } }

    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            modifier = Modifier.size(56.dp),
            enabled = isAnySelected,
            onClick = onDeleteAllSelectedAction::invoke
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            modifier = Modifier
                .height(56.dp)
                .weight(1f),
            enabled = isAnySelected,
            onClick = onBuyAllSelectedAction::invoke
        ) {
            if (isAnySelected) {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        text = stringResource(action_buy),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        text = buildPriceText(sumStart, sumFinal),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button
                    )
                }
            } else {
                Text(text = stringResource(id = action_buy_empty))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BasketProductElement(
    product: BasketBunchItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSelected: (Boolean) -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
) {
    Card(
        modifier = Modifier.wrapContentSize(),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelected,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Box(
                modifier = Modifier.size(80.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = product.product.imageUrl,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp)
                    .fillMaxHeight()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = buildPriceText(
                            priceStart = product.product.price.startPrice,
                            priceFinal = product.product.price.finalPrice
                        ),
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    modifier = Modifier,
                    text = product.product.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .align(Alignment.CenterVertically)
            ) {
                IconButton(onClick = onAdd) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 4.dp),
                    text = product.count.toString(),
                    style = MaterialTheme.typography.h6
                )
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

private fun buildPriceText(priceStart: Double, priceFinal: Double): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(Formatters.currency.format(priceFinal))
        }
        append("  ")
        withStyle(
            SpanStyle(
                fontSize = 12.sp,
                textDecoration = TextDecoration.LineThrough
            )
        ) {
            append(Formatters.currency.format(priceStart))
        }
    }
}

@Preview
@Composable
private fun BasketContentPreview() {
    val state = BasketViewState()
    BasketContent(
        state = state,
        onQueryChanged = {},
        onSearchAction = {},
        onOpenFiltersAction = {},
        onProductClicked = {},
        onProductSelected = { _, _ -> },
        onDeleteAllSelectedAction = {},
        onBuyAllSelectedAction = {},
        onAdd = {},
        onRemove = {}
    )
}