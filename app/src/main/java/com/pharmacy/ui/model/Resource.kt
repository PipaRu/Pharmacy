package com.pharmacy.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Resource<out T : Parcelable> : Parcelable {

    @Parcelize
    object Nothing : Resource<kotlin.Nothing>()

    @Parcelize
    object Loading : Resource<kotlin.Nothing>()

    @Parcelize
    data class Data<T : Parcelable>(val value: T) : Resource<T>()

    @Parcelize
    data class Error(val throwable: Throwable) : Resource<kotlin.Nothing>()

}

fun <T : Parcelable> Resource<T>.getOrNull(): T? = (this as? Resource.Data)?.value