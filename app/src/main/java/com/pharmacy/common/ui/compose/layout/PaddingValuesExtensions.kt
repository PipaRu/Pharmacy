package com.pharmacy.common.ui.compose.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

@Composable
operator fun PaddingValues.plus(paddingValues: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = this.calculateLeftPadding(layoutDirection) + paddingValues.calculateLeftPadding(layoutDirection),
        top = this.calculateTopPadding() + paddingValues.calculateTopPadding(),
        end = this.calculateRightPadding(layoutDirection) + paddingValues.calculateRightPadding(layoutDirection),
        bottom = this.calculateBottomPadding() + paddingValues.calculateBottomPadding()
    )
}