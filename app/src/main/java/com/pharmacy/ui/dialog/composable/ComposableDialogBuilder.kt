package com.pharmacy.ui.dialog.composable

import android.os.Parcelable
import androidx.compose.runtime.Composable

interface ComposableDialogBuilder : Parcelable {
    @Composable
    fun Build(onClose: () -> Unit)
}