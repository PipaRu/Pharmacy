package com.pharmacy.ui.model

import android.os.Parcelable
import com.pharmacy.common.ui.model.Selectable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectableItem<T : Parcelable>(
    val value: T,
    override val isSelected: Boolean = false,
) : Parcelable, Selectable