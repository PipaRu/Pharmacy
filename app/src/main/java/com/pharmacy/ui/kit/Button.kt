@file:OptIn(ExperimentalMaterialApi::class)

package com.pharmacy.ui.kit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

enum class ActionButtonSize {
    SMALL,
    MEDIUM,
    BIG,
}

enum class ActionButtonState {
    LOADING,
    DISABLED,
    ENABLED,
}

private data class ActionButtonSizes(
    val size: DpSize,
    val indicatorSize: DpSize,
    val iconSize: DpSize,
    val textPaddings: PaddingValues,
    val textSize: TextUnit,
)

private val sizes = mapOf(
    ActionButtonSize.SMALL to ActionButtonSizes(
        size = DpSize(width = Dp.Unspecified, height = 36.dp),
        indicatorSize = DpSize(width = 24.dp, height = 24.dp),
        iconSize = DpSize(width = 24.dp, height = 24.dp),
        textPaddings = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
        textSize = 12.sp,
    ),
    ActionButtonSize.MEDIUM to ActionButtonSizes(
        size = DpSize(width = Dp.Unspecified, height = 44.dp),
        indicatorSize = DpSize(width = 32.dp, height = 32.dp),
        iconSize = DpSize(width = 32.dp, height = 32.dp),
        textPaddings = PaddingValues(horizontal = 6.dp, vertical = 3.dp),
        textSize = 14.sp,
    ),
    ActionButtonSize.BIG to ActionButtonSizes(
        size = DpSize(width = Dp.Unspecified, height = 56.dp),
        indicatorSize = DpSize(width = 44.dp, height = 44.dp),
        iconSize = DpSize(width = 44.dp, height = 44.dp),
        textPaddings = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        textSize = 16.sp,
    )
)

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    size: ActionButtonSize = ActionButtonSize.MEDIUM,
    state: ActionButtonState = ActionButtonState.ENABLED,
    icon: ImageVector? = null,
    text: String,
    onClick: () -> Unit,
) {
    val sizes = requireNotNull(sizes[size])
    Button(
        modifier = modifier.then(Modifier.size(sizes.size)),
        onClick = onClick,
        enabled = state == ActionButtonState.ENABLED,
        contentPadding = PaddingValues(),
    ) {
        if (state == ActionButtonState.LOADING) {
            CircularProgressIndicator(modifier = Modifier.size(sizes.indicatorSize))
        } else {
            Row {
                if (icon != null) {
                    Icon(
                        modifier = Modifier.size(sizes.iconSize),
                        imageVector = icon,
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = text,
                    style = MaterialTheme.typography.button.copy(fontSize = sizes.textSize)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ActionButtonsPreview() {
    Column {
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.SMALL,
            state = ActionButtonState.ENABLED,
            icon = Icons.Rounded.Home,
            text = "Small_Enabled",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.SMALL,
            state = ActionButtonState.DISABLED,
            icon = Icons.Rounded.Home,
            text = "Small_Disabled",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.SMALL,
            state = ActionButtonState.LOADING,
            icon = Icons.Rounded.Home,
            text = "Small_Loading",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.MEDIUM,
            state = ActionButtonState.ENABLED,
            icon = Icons.Rounded.Home,
            text = "Medium_Enabled",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.MEDIUM,
            state = ActionButtonState.DISABLED,
            icon = Icons.Rounded.Home,
            text = "Medium_Disabled",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.MEDIUM,
            state = ActionButtonState.LOADING,
            icon = Icons.Rounded.Home,
            text = "Medium_Loading",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.BIG,
            state = ActionButtonState.ENABLED,
            icon = Icons.Rounded.Home,
            text = "Big_Enabled",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.BIG,
            state = ActionButtonState.DISABLED,
            icon = Icons.Rounded.Home,
            text = "Big_Disabled",
            onClick = {}
        )
        ActionButton(
            modifier = Modifier,
            size = ActionButtonSize.BIG,
            state = ActionButtonState.LOADING,
            icon = Icons.Rounded.Home,
            text = "Big_Loading",
            onClick = {}
        )
    }
}