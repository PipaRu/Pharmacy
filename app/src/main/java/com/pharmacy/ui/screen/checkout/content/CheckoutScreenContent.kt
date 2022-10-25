package com.pharmacy.ui.screen.checkout.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pharmacy.R
import com.pharmacy.common.formatter.Formatters
import com.pharmacy.common.formatter.QuantityFormatter
import com.pharmacy.ui.model.*
import com.pharmacy.ui.screen.checkout.CheckoutViewModel
import com.pharmacy.ui.screen.checkout.model.mvi.CheckoutViewState
import org.orbitmvi.orbit.compose.collectAsState
import java.util.*

@Composable
fun CheckoutScreenContent(
    viewModel: CheckoutViewModel,
) {
    val state: CheckoutViewState by viewModel.collectAsState()
    CheckoutContent(
        state = state,
        onBack = viewModel::back,
        onLoginAction = viewModel::toLogin,
        setAddressAction = viewModel::addressSelection,
        onOrderStartAction = viewModel::checkout,
        onNextAfterCheckout = viewModel::nextAfterCheckout
    )
}

@Composable
private fun CheckoutContent(
    state: CheckoutViewState,
    onBack: () -> Unit,
    onLoginAction: () -> Unit,
    setAddressAction: () -> Unit,
    onOrderStartAction: () -> Unit,
    onNextAfterCheckout: () -> Unit,
) {
    val profile = state.profile
    Scaffold(
        modifier = Modifier,
        topBar = { TopBarContent(onBack = onBack) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    state.isContentLoading -> LoadingContent()
                    state.order != null -> CheckoutCompletedContentComponent(
                        order = state.order,
                        onNext = onNextAfterCheckout
                    )
                    profile == null -> CheckoutUnauthorizedContentComponent(
                        onLoginAction = onLoginAction
                    )
                    else -> CheckoutAuthorizedContentComponent(
                        profile = profile,
                        products = state.products,
                        isCheckoutProcessLoading = state.isCheckoutProcessLoading,
                        isCheckoutAvailable = state.isCheckoutAvailable,
                        setAddressAction = setAddressAction,
                        onOrderStartAction = onOrderStartAction
                    )
                }
            }
        }
    )
}

@Composable
private fun TopBarContent(
    onBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.content_checkout_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun CheckoutUnauthorizedContentComponent(
    onLoginAction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.content_checkout_unauthorized_message),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        Button(onClick = onLoginAction) {
            Text(text = stringResource(R.string.content_checkout_unauthorized_action))
        }
    }
}

@Composable
private fun CheckoutCompletedContentComponent(
    order: OrderItem,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(128.dp),
            imageVector = Icons.Rounded.AssignmentTurnedIn,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(
                id = R.string.content_checkout_completed_message,
                formatArgs = arrayOf(order.id)
            ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(0.5f),
            onClick = onNext
        ) {
            Text(text = stringResource(R.string.content_checkout_completed_action))
        }
    }
}

@Composable
private fun CheckoutAuthorizedContentComponent(
    profile: ProfileItem,
    products: List<BasketBunchItem> = emptyList(),
    isCheckoutProcessLoading: Boolean = false,
    isCheckoutAvailable: Boolean = false,
    setAddressAction: () -> Unit,
    onOrderStartAction: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            CheckoutAddressComponent(
                address = profile.address,
                isMutable = !isCheckoutProcessLoading,
                setAddressAction = setAddressAction
            )
        }
        items(products) { product ->
            CheckoutProductComponent(product)
        }
        item {
            CheckoutSummaryComponent(products)
        }
        item {
            CheckoutActionComponent(
                isAvailable = isCheckoutAvailable && !isCheckoutProcessLoading,
                isLoading = isCheckoutProcessLoading,
                onAction = onOrderStartAction
            )
        }
    }
}

@Composable
private fun CheckoutAddressComponent(
    address: AddressItem?,
    isMutable: Boolean,
    setAddressAction: () -> Unit,
) {
    Card(
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            if (address == null) {
                Text(
                    text = stringResource(R.string.content_checkout_address_require_message),
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = setAddressAction,
                    enabled = isMutable
                ) {
                    Text(text = stringResource(R.string.content_checkout_address_require_action))
                }
            } else {
                Text(
                    text = stringResource(R.string.content_checkout_address_label),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = address.name,
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = setAddressAction,
                    enabled = isMutable
                ) {
                    Text(text = stringResource(R.string.content_checkout_address_action_change))
                }
            }
        }
    }
}

@Composable
private fun CheckoutProductComponent(product: BasketBunchItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight()
            ) {
                val placeholder = rememberVectorPainter(Icons.Default.Image)
                AsyncImage(
                    modifier = Modifier.size(88.dp),
                    model = product.product.imageUrl,
                    contentDescription = null,
                    placeholder = placeholder,
                    error = placeholder,
                    fallback = placeholder
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = Formatters.currency.format(product.product.price.finalPrice)
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = QuantityFormatter.format(product.count)
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = product.product.name,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun CheckoutSummaryComponent(
    products: List<BasketBunchItem>,
) {
    val totalCount by derivedStateOf {
        products.sumOf { it.count }
    }
    val totalStartPrice by derivedStateOf {
        products.sumOf {
            it.count * it.product.price.startPrice
        }
    }
    val totalFinalPrice by derivedStateOf {
        products.sumOf {
            it.count * it.product.price.finalPrice
        }
    }
    val totalDiscount by derivedStateOf {
        totalStartPrice - totalFinalPrice
    }
    Card(
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(R.string.content_checkout_summary_title),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = QuantityFormatter.format(totalCount),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.End
                )
            }
            Row {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(R.string.content_checkout_summary_price),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = Formatters.currency.format(totalStartPrice),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.End
                )
            }
            Row {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(R.string.content_checkout_summary_discount),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = Formatters.currency.format(totalDiscount),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.End
                )
            }
            Row {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(R.string.content_checkout_summary_total),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = Formatters.currency.format(totalFinalPrice),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun CheckoutActionComponent(
    isAvailable: Boolean,
    isLoading: Boolean,
    onAction: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp)
                .align(Alignment.Center),
            enabled = isAvailable,
            onClick = onAction
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp)
                )
            } else {
                Text(text = stringResource(id = R.string.content_checkout_action))
            }
        }
    }
}


@Preview
@Composable
private fun CheckoutContentPreview() {
    val state = CheckoutViewState(
        profile = ProfileItem.Empty.copy(
            address = AddressItem(
                id = 0L,
                name = "843397, Тверская область, город Серебряные Пруды, пл. Домодедовская, 40"
            )
        ),
        products = listOf(
            BasketBunchItem(
                id = 1,
                product = ProductItem(
                    id = 1,
                    name = "Product #1",
                    imageUrl = "Image",
                    description = "Product Description",
                    category = "Category",
                    price = PriceItem(
                        finalPrice = 300.0,
                        startPrice = 600.0,
                        discount = 50.0
                    )
                ),
                count = 2
            ),
            BasketBunchItem(
                id = 2,
                product = ProductItem(
                    id = 2,
                    name = "Product #2",
                    imageUrl = "Image",
                    description = "Product Description",
                    category = "Category",
                    price = PriceItem(
                        finalPrice = 1600.0,
                        startPrice = 1600.0,
                        discount = 0.0
                    )
                ),
                count = 1
            )
        ),
        isContentLoading = false,
        order = OrderItem(
            id = 123,
            date = Date(),
            status = OrderItem.Status.CREATED
        ),
        isCheckoutProcessLoading = false
    )
    CheckoutContent(
        state = state,
        onBack = {},
        onLoginAction = {},
        setAddressAction = {},
        onOrderStartAction = {},
        {}
    )
}