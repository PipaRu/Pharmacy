@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class,
)

package com.pharmacy.ui.screen.admin_products.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import com.pharmacy.common.formatter.PercentFormatter
import com.pharmacy.common.ui.compose.interaction.DisabledInteractionSource
import com.pharmacy.common.ui.compose.text.field.OutlinedTextField
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.model.SelectableItem
import com.pharmacy.ui.screen.admin_products.AdminProductsViewModel
import com.pharmacy.ui.screen.admin_products.model.mvi.AdminProductsViewState
import org.orbitmvi.orbit.compose.collectAsState
import com.pharmacy.R.string.content_profile_admin_products_action_back as action_back
import com.pharmacy.R.string.content_profile_admin_products_action_delete_all_selected as action_delete_all_selected
import com.pharmacy.R.string.content_profile_admin_products_action_disable_selection_mode as action_disable_selection_mode
import com.pharmacy.R.string.content_profile_admin_products_action_enable_selection_mode as action_enable_selection_mode
import com.pharmacy.R.string.content_profile_admin_products_description_product_image as description_product_image
import com.pharmacy.R.string.content_profile_admin_products_text_search_placeholder as text_search_placeholder

@Composable
fun AdminProductsScreenContent(viewModel: AdminProductsViewModel) {
    val state: AdminProductsViewState by viewModel.collectAsState()
    AdminProductsContent(
        state = state,
        onBack = viewModel::back,
        onQueryChanged = viewModel::onQueryChanged,
        onSearchAction = viewModel::search,
        onSelectionMode = viewModel::setSelectionMode,
        onDeleteAllSelectedAction = viewModel::deleteAllSelectedAction,
        onProductClick = viewModel::onProductClick,
        onProductSelect = viewModel::onSelectProduct,
        onAddProductClick = viewModel::addProduct,
    )
}

@Composable
private fun AdminProductsContent(
    state: AdminProductsViewState,
    onBack: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearchAction: () -> Unit,
    onSelectionMode: (Boolean) -> Unit,
    onDeleteAllSelectedAction: () -> Unit,
    onProductClick: (ProductItem) -> Unit,
    onProductSelect: (ProductItem) -> Unit,
    onAddProductClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBarContent(
                isSelectionMode = state.isSelectionMode,
                isSelectionEnabled = !state.isContentLoading && !state.isContentEditingLoading,
                isDeletionEnabled = !state.isContentLoading && !state.isContentEditingLoading,
                query = state.query,
                onQueryChanged = onQueryChanged,
                onSearchAction = onSearchAction,
                onBack = onBack,
                onSelectionMode = onSelectionMode,
                onDeleteAllSelectedAction = onDeleteAllSelectedAction
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        content = { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings),
                contentAlignment = Alignment.TopCenter
            ) {
                if (state.isContentLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    AllProductsContent(
                        items = state.items,
                        isSelectionMode = state.isSelectionMode,
                        isProductActionEnabled = !state.isContentEditingLoading,
                        onProductClick = onProductClick,
                        onProductSelect = onProductSelect
                    )
                }
                if (state.isContentEditingLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                    )
                }
            }
        }
    )
}

@Composable
private fun TopAppBarContent(
    isSelectionMode: Boolean,
    isDeletionEnabled: Boolean,
    isSelectionEnabled: Boolean,
    query: String,
    onBack: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearchAction: () -> Unit,
    onSelectionMode: (Boolean) -> Unit,
    onDeleteAllSelectedAction: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(action_back)
                )
            }
        },
        title = {
            OutlinedTextField(
                modifier = Modifier.height(36.dp),
                value = query,
                onValueChange = onQueryChanged,
                placeholder = { Text(text = stringResource(text_search_placeholder)) },
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
            if (isSelectionMode) {
                IconButton(
                    onClick = { onSelectionMode.invoke(false) },
                    enabled = isSelectionEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(action_disable_selection_mode)
                    )
                }
                IconButton(
                    onClick = onDeleteAllSelectedAction,
                    enabled = isDeletionEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(action_delete_all_selected)
                    )
                }
            } else {
                IconButton(
                    onClick = { onSelectionMode.invoke(true) },
                    enabled = isSelectionEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.SelectAll,
                        contentDescription = stringResource(action_enable_selection_mode),
                    )
                }
            }
        }
    )
}

@Composable
private fun AllProductsContent(
    items: List<SelectableItem<ProductItem>>,
    isSelectionMode: Boolean,
    isProductActionEnabled: Boolean,
    onProductClick: (ProductItem) -> Unit,
    onProductSelect: (ProductItem) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 96.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(
            items = items,
            key = { item -> item.value.id }
        ) { item ->
            if (isSelectionMode) {
                SelectableProductCard(
                    modifier = Modifier.animateItemPlacement(),
                    product = item,
                    enabled = isProductActionEnabled,
                    onClick = { onProductSelect.invoke(item.value) }
                )
            } else {
                ProductCard(
                    modifier = Modifier.animateItemPlacement(),
                    product = item.value,
                    enabled = isProductActionEnabled,
                    onClick = { onProductClick.invoke(item.value) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    product: ProductItem,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        elevation = 4.dp
    ) {
        ProductContent(product = product)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SelectableProductCard(
    modifier: Modifier = Modifier,
    product: SelectableItem<ProductItem>,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        elevation = 4.dp
    ) {
        Box {
            Checkbox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                checked = product.isSelected,
                enabled = enabled,
                onCheckedChange = { onClick.invoke() },
                interactionSource = DisabledInteractionSource
            )
            ProductContent(product = product.value)
        }
    }
}

@Composable
private fun ProductContent(
    product: ProductItem,
) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        val defaultImage = rememberVectorPainter(image = Icons.Default.Image)
        AsyncImage(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.CenterHorizontally),
            model = product.imageUrl,
            contentDescription = stringResource(id = description_product_image),
            placeholder = defaultImage,
            error = defaultImage,
            fallback = defaultImage
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = product.name,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = product.description,
            style = MaterialTheme.typography.subtitle2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = with(product.price) { buildPriceText(startPrice, finalPrice, discount) },
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

private fun buildPriceText(
    priceStart: Double,
    priceFinal: Double,
    discount: Double,
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(Formatters.currency.format(priceFinal))
        }
        if (discount > 0.0) {
            append("  ")
            withStyle(
                SpanStyle(
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.LineThrough
                )
            ) {
                append(Formatters.currency.format(priceStart))
            }
            append("  ")
            withStyle(
                SpanStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(PercentFormatter.format(discount))
            }
        }
    }
}

@Preview
@Composable
private fun AdminProductsContentPreview() {
    val emptyItem = ProductItem.Empty
    val item0 = emptyItem.copy(
        name = "Product",
        description = "Description",
        price = emptyItem.price.copy(
            startPrice = 100.0
        )
    )
    val item1 = emptyItem.copy(
        name = "Product",
        description = "Description",
        price = emptyItem.price.copy(
            startPrice = 100.0,
            discount = 50.0
        )
    )
    val item2 = emptyItem.copy(
        name = "Product Product Product Product Product",
        description = "Description Description Description Description Description Description",
        price = emptyItem.price.copy(
            startPrice = 100.0,
            discount = 50.0
        )
    )
    val state = AdminProductsViewState(
        isContentLoading = false,
        items = listOf(item0, item1, item2, item0, item1, item2).map(::SelectableItem)
    )
    AdminProductsContent(
        state = state,
        onBack = {},
        onQueryChanged = {},
        onSearchAction = {},
        onSelectionMode = {},
        onDeleteAllSelectedAction = {},
        onProductClick = {},
        onProductSelect = {},
        onAddProductClick = {},
    )
}