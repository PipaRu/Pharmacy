package com.pharmacy.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface ParcelableList<T : Parcelable> : List<T>, Parcelable

@Parcelize
class DefaultParcelableList<T : Parcelable>(val source: List<T>) : ParcelableList<T>, List<T> by source

fun <T : Parcelable> parcelableListOf(vararg values: T): ParcelableList<T> = DefaultParcelableList(values.toList())

fun <T : Parcelable> List<T>.toParcelableList(): ParcelableList<T> = DefaultParcelableList(this)