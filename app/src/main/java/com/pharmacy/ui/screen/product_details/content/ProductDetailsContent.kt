@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.screen.product_details.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
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
import com.google.accompanist.flowlayout.FlowRow
import com.pharmacy.R
import com.pharmacy.common.extensions.emptyString
import com.pharmacy.common.formatter.Formatters
import com.pharmacy.common.formatter.PercentFormatter
import com.pharmacy.common.ui.compose.interaction.DisabledInteractionSource
import com.pharmacy.data.model.BasketBunch
import com.pharmacy.ui.dialog.InputDialog
import com.pharmacy.ui.model.BasketBunchItem
import com.pharmacy.ui.model.CategoryItem
import com.pharmacy.ui.model.ProductItem
import com.pharmacy.ui.screen.admin_product_details.AdminProductDetailsViewModel
import com.pharmacy.ui.screen.admin_product_details.model.mvi.AdminProductDetailsViewState
import com.pharmacy.ui.screen.product_details.ProductDetailsViewModel
import com.pharmacy.ui.screen.product_details.model.mvi.ProductDetailsViewState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ProductDetailsContent(viewModel: ProductDetailsViewModel) {
    val state: ProductDetailsViewState by viewModel.collectAsState()
    ScreenContent(
        state = state,
        onBackAction = viewModel::back,
        onAddToBasket = viewModel::addToBasket,
        onDeleteFromBasket = viewModel::deleteFromBasket,
    )
}

@Composable
private fun ScreenContent(
    state: ProductDetailsViewState,
    onBackAction: () -> Unit,
    onAddToBasket: () -> Unit,
    onDeleteFromBasket: () -> Unit,
) {
    val product: ProductItem = state.product

    Scaffold(
        topBar = {
            TopAppBarContent(
                title = product.name,
                onBackAction = onBackAction,
            )
        }
    ) { paddings ->
        if (state.isContentLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter
            ) {
                ProductDetailsContent(
                    product = product,
                    basketBunch = state.basketBunch,
                    inBasketLoading = state.isInBasketLoading,
                    onAddToBasket = onAddToBasket,
                    onDeleteFromBasket = onDeleteFromBasket,
                )
            }
        }
    }
}

@Composable
private fun TopAppBarContent(
    title: String,
    onBackAction: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackAction) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
    )
}

@Composable
private fun ProductDetailsContent(
    product: ProductItem,
    basketBunch: BasketBunchItem?,
    inBasketLoading: Boolean,
    onAddToBasket: () -> Unit,
    onDeleteFromBasket: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val defaultImage = rememberVectorPainter(Icons.Default.Image)
        AsyncImage(
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.CenterHorizontally),
            model = product.imageUrl,
            placeholder = defaultImage,
            error = defaultImage,
            fallback = defaultImage,
            contentDescription = null
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = with(product.price) {
                buildPriceText(
                    startPrice,
                    finalPrice,
                    discount
                )
            },
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Box(
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (basketBunch != null) {
                Row(
                    modifier = Modifier
                ) {
                    IconButton(onClick = onDeleteFromBasket) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 4.dp),
                        text = basketBunch.count.toString(),
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
                    onClick = onAddToBasket
                ) {
                    if (inBasketLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.content_showcase_action_product_add_to_basket)
                        )
                    }
                }
            }
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = product.name,
            style = MaterialTheme.typography.h6,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = product.description,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Justify,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 4.dp,
        ) {
            product.categories.forEach { category ->
                Chip(
                    onClick = { },
                    interactionSource = DisabledInteractionSource
                ) {
                    Text(text = category.name)
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ScreenContentPreview() {
    val state = ProductDetailsViewState(
        isContentLoading = false
    )
    ScreenContent(
        state = state,
        onBackAction = {},
        onAddToBasket = {},
        onDeleteFromBasket = {},
    )
}

private fun buildPriceText(
    priceStart: Double,
    priceFinal: Double,
    discount: Double,
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(Formatters.currency.format(priceFinal))
        }
        if (discount > 0.0) {
            append("  ")
            withStyle(
                SpanStyle(
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.LineThrough
                )
            ) {
                append(Formatters.currency.format(priceStart))
            }
            append("  ")
            withStyle(
                SpanStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(PercentFormatter.format(discount))
            }
        }
    }
}
