@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.pharmacy.ui.screen.showcase.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import com.pharmacy.R
import com.pharmacy.common.formatter.Formatters
import com.pharmacy.common.formatter.PercentFormatter
import com.pharmacy.common.ui.compose.text.field.OutlinedTextField
import com.pharmacy.ui.screen.showcase.ShowcaseViewModel
import com.pharmacy.ui.screen.showcase.model.ShowcaseItem
import com.pharmacy.ui.screen.showcase.model.mvi.ShowcaseViewState
import com.valentinilk.shimmer.shimmer
import org.orbitmvi.orbit.compose.collectAsState
import com.pharmacy.R.string.content_showcase_action_product_add_to_basket as action_product_add_to_basket
import com.pharmacy.R.string.content_showcase_action_product_remove_from_basket as action_product_remove_from_basket
import com.pharmacy.R.string.content_showcase_description_filter as description_filter
import com.pharmacy.R.string.content_showcase_description_product_image as description_product_image
import com.pharmacy.R.string.content_showcase_text_search_placeholder as text_search_placeholder

@Composable
fun ShowcaseScreenContent(viewModel: ShowcaseViewModel) {
    val state: ShowcaseViewState by viewModel.collectAsState()

    ShowcaseContent(
        state = state,
        onRetryLoading = viewModel::retryLoading,
        onQueryChanged = viewModel::query,
        onSearchAction = viewModel::search,
        onFilters = viewModel::openFilters,
        onAddToBasket = viewModel::addProductToBasket,
        onRemoveFromBasket = viewModel::removeProductFromBasket,
        onProductAction = viewModel::openProductDetails,
        onNextItemIndex = viewModel::onNextItemIndex
    )
}

@Composable
private fun ShowcaseContent(
    state: ShowcaseViewState,
    onRetryLoading: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearchAction: () -> Unit,
    onFilters: () -> Unit,
    onAddToBasket: (ShowcaseItem.Product) -> Unit,
    onRemoveFromBasket: (ShowcaseItem.Product) -> Unit,
    onProductAction: (ShowcaseItem.Product) -> Unit,
    onNextItemIndex: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBarContent(
                query = state.query,
                onQueryChanged = onQueryChanged,
                onSearchAction = onSearchAction,
                onFilterAction = onFilters
            )
        },
        content = { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                when {
                    state.isProductsLoading -> {
                        AllProductsLoadingContent()
                    }
                    state.productsLoadingError != null -> {
                        AllProductsErrorContent(
                            error = state.productsLoadingError,
                            onRetry = onRetryLoading
                        )
                    }
                    else -> {
                        AllProductsContent(
                            items = state.items,
                            onRetry = onRetryLoading,
                            onAddToBasket = onAddToBasket,
                            onRemoveFromBasket = onRemoveFromBasket,
                            onProductAction = onProductAction,
                            onNextItemIndex = onNextItemIndex
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
    onFilterAction: () -> Unit,
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                modifier = Modifier
                    .height(36.dp)
                    .fillMaxWidth(),
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
            IconButton(onClick = onFilterAction) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = stringResource(description_filter)
                )
            }
        }
    )
}

@Composable
private fun AllProductsContent(
    items: List<ShowcaseItem>,
    onRetry: () -> Unit,
    onAddToBasket: (ShowcaseItem.Product) -> Unit,
    onRemoveFromBasket: (ShowcaseItem.Product) -> Unit,
    onProductAction: (ShowcaseItem.Product) -> Unit,
    onNextItemIndex: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(
            items = items,
            key = { index, item -> item.id },
            contentType = { index, item -> item::class },
            span = { index, item ->
                when (item) {
                    is ShowcaseItem.Product,
                    is ShowcaseItem.ProductPlaceHolder,
                    -> GridItemSpan(1)
                    is ShowcaseItem.Error,
                    is ShowcaseItem.Loader,
                    -> GridItemSpan(maxLineSpan)
                }
            }
        ) { index, item ->
            onNextItemIndex.invoke(index)
            when (item) {
                is ShowcaseItem.Product -> {
                    ProductCard(
                        modifier = Modifier.animateItemPlacement(),
                        product = item,
                        onAction = { onProductAction.invoke(item) },
                        onAddToBasket = { onAddToBasket.invoke(item) },
                        onRemoveFromBasket = { onRemoveFromBasket.invoke(item) },
                    )
                }
                is ShowcaseItem.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.content_showcase_loading_items_message_error))
                        Button(onClick = onRetry) {
                            Text(text = stringResource(R.string.content_showcase_loading_items_action_try_again))
                        }
                    }
                }
                is ShowcaseItem.Loader -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ShowcaseItem.ProductPlaceHolder -> {
                    ProductCard(
                        modifier = Modifier
                            .animateItemPlacement()
                            .shimmer(),
                        product = ShowcaseItem.Product.Empty,
                        onAction = {},
                        onAddToBasket = {},
                        onRemoveFromBasket = {},
                    )
                }
            }
        }
    }
}

@Composable
private fun AllProductsErrorContent(
    error: Throwable,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.content_showcase_loading_message_error))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.content_showcase_loading_action_try_again))
        }
    }
}

@Composable
private fun AllProductsLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ProductCard(
    modifier: Modifier = Modifier,
    product: ShowcaseItem.Product,
    onAction: () -> Unit,
    onAddToBasket: () -> Unit,
    onRemoveFromBasket: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onAction,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            val defaultImage = rememberVectorPainter(Icons.Default.Image)
            AsyncImage(
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally),
                model = product.item.imageUrl,
                contentDescription = stringResource(description_product_image),
                placeholder = defaultImage,
                error = defaultImage,
                fallback = defaultImage
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp),
                text = product.item.name,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = with(product.item.price) {
                    buildPriceText(startPrice,
                        finalPrice,
                        discount)
                },
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (product.inBasket) {
                    Row(
                        modifier = Modifier
                    ) {
                        IconButton(onClick = onRemoveFromBasket) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = null
                            )
                        }
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 4.dp),
                            text = product.inBasketCount.toString(),
                            style = MaterialTheme.typography.h6
                        )
                        IconButton(onClick = onAddToBasket) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(36.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                        onClick = if (product.inBasket) onRemoveFromBasket else onAddToBasket
                    ) {
                        if (product.isBasketLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                        } else {
                            val text = if (product.inBasket) {
                                action_product_remove_from_basket
                            } else {
                                action_product_add_to_basket
                            }
                            Text(text = stringResource(text))
                        }
                    }
                }
            }
        }
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
private fun ShowcaseContentPreview() {

    ShowcaseContent(
        state = ShowcaseViewState(query = ""),
        onRetryLoading = {  },
        onQueryChanged = { },
        onSearchAction = {  },
        onFilters = {  },
        onAddToBasket = {},
        onRemoveFromBasket = {},
        onProductAction = {},
        onNextItemIndex = {}
    )
}