@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.screen.orders.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pharmacy.R
import com.pharmacy.common.formatter.DateFormatter
import com.pharmacy.ui.model.OrderItem
import com.pharmacy.ui.screen.orders.OrdersViewModel
import com.pharmacy.ui.screen.orders.model.mvi.OrdersViewState
import org.orbitmvi.orbit.compose.collectAsState
import java.util.*

@Composable
fun OrdersScreenContent(viewModel: OrdersViewModel) {
    val state: OrdersViewState by viewModel.collectAsState()
    OrdersContent(
        state = state,
        onBack = viewModel::backAction,
        onTryAgainLoading = viewModel::tryAgainLoadingAction,
        onOrderClick = viewModel::selectOrderAction,
        onCancelOrder = viewModel::cancelOrderAction,
        onDeleteOrder = viewModel::deleteOrder,
    )
}

@Composable
private fun OrdersContent(
    state: OrdersViewState,
    onBack: () -> Unit,
    onTryAgainLoading: () -> Unit,
    onOrderClick: (OrderItem) -> Unit,
    onCancelOrder: (OrderItem) -> Unit,
    onDeleteOrder: (OrderItem) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = { OrdersTopBarComponent(onBack = onBack) },
        content = { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                when {
                    state.isContentLoading -> OrdersLoadingContent()
                    state.isContentLoadingFailed -> OrdersErrorContent(
                        onTryAgain = onTryAgainLoading
                    )
                    state.orders.isEmpty() -> OrdersEmptyContent()
                    else -> OrdersListContent(
                        orders = state.orders,
                        onClick = onOrderClick,
                        onCancel = onCancelOrder,
                        onDelete = onDeleteOrder
                    )
                }
            }
        }
    )
}

@Composable
private fun OrdersTopBarComponent(
    onBack: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = { Text(text = stringResource(R.string.content_orders_title)) }
    )
}

@Composable
private fun OrdersLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun OrdersEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(128.dp),
                imageVector = Icons.Rounded.ContentPasteOff,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.content_orders_message_empty),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun OrdersErrorContent(
    onTryAgain: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(128.dp),
                imageVector = Icons.Rounded.Error,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.content_orders_message_error),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.height(48.dp),
                onClick = onTryAgain
            ) {
                Text(text = stringResource(R.string.content_orders_action_try_again))
            }
        }
    }
}

@Composable
private fun OrdersListContent(
    orders: List<OrderItem>,
    onClick: (OrderItem) -> Unit,
    onCancel: (OrderItem) -> Unit,
    onDelete: (OrderItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(orders) { order ->
            OrderItemComponent(
                order = order,
                onClick = { onClick.invoke(order) },
                onCancel = { onCancel.invoke(order) },
                onDelete = { onDelete.invoke(order) },
            )
        }
    }
}

@Composable
private fun OrderItemComponent(
    order: OrderItem,
    onClick: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        onClick = onClick,
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = stringResource(
                        id = R.string.content_orders_item_placeholder,
                        formatArgs = arrayOf(order.id)
                    ),
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = DateFormatter.format(order.date),
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(8.dp))
                OrderStatusComponent(
                    status = order.status
                )
            }
            if (order.status == OrderItem.Status.CANCELLED) {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(56.dp),
                    onClick = onDelete,
                    contentPadding = PaddingValues(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = null
                    )
                }
            } else {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(56.dp),
                    onClick = onCancel,
                    contentPadding = PaddingValues(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderStatusComponent(
    status: OrderItem.Status,
) {
    val text = when (status) {
        OrderItem.Status.UNDEFINED -> stringResource(R.string.order_status_undefined)
        OrderItem.Status.CREATED -> stringResource(R.string.order_status_created)
        OrderItem.Status.PROGRESS -> stringResource(R.string.order_status_progress)
        OrderItem.Status.COMPLETED -> stringResource(R.string.order_status_completed)
        OrderItem.Status.CANCELLED -> stringResource(R.string.order_status_cancelled)
        OrderItem.Status.FAILED -> stringResource(R.string.order_status_failed)
    }

    val backgroundColor = when (status) {
        OrderItem.Status.UNDEFINED -> colorResource(R.color.order_status_undefined)
        OrderItem.Status.CREATED -> colorResource(R.color.order_status_created)
        OrderItem.Status.PROGRESS -> colorResource(R.color.order_status_progress)
        OrderItem.Status.COMPLETED -> colorResource(R.color.order_status_completed)
        OrderItem.Status.CANCELLED -> colorResource(R.color.order_status_cancelled)
        OrderItem.Status.FAILED -> colorResource(R.color.order_status_failed)
    }
    val textColor = when (status) {
        OrderItem.Status.UNDEFINED -> Color.White
        OrderItem.Status.CREATED -> Color.Black
        OrderItem.Status.PROGRESS -> Color.White
        OrderItem.Status.COMPLETED -> Color.Black
        OrderItem.Status.CANCELLED -> Color.Black
        OrderItem.Status.FAILED -> Color.White
    }
    Card(
        backgroundColor = backgroundColor
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            text = text,
            color = textColor,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun OrdersContentPreview() {

    val orders = OrderItem.Status.values().mapIndexed { index, status ->
        OrderItem(
            id = index.toLong(),
            date = Date(),
            status = status
        )
    }

    val state = OrdersViewState(
        isContentLoading = false,
        isContentLoadingFailed = false,
        orders = orders
    )

    OrdersContent(
        state = state,
        onBack = { },
        onTryAgainLoading = { },
        onOrderClick = { },
        onCancelOrder = { },
        onDeleteOrder = { }
    )
}