package com.pharmacy.common.extensions

import com.pharmacy.common.filter.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.filter as nativeFilter

fun <T> flowOf(block: suspend () -> T) = flow { emit(block.invoke()) }

fun <T> Flow<T>.delay(timeMillis: Long): Flow<T> = onEach { kotlinx.coroutines.delay(timeMillis) }

fun <T, R> Flow<List<T>>.mapElements(block: suspend (T) -> R): Flow<List<R>> = map { elements ->
    elements.map { element -> block.invoke(element) }
}

fun <T> Flow<T>.filter(filter: Filter<T>): Flow<T> = nativeFilter(filter::filter)

fun <T> Flow<T>.onFirst(block: suspend (T) -> Unit): Flow<T> {
    var isCollected = false
    return onEach { value ->
        if (!isCollected) {
            block.invoke(value)
            isCollected = true
        }
    }
}