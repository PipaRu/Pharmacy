package com.pharmacy.common.extensions

import androidx.paging.PagingSource
import com.pharmacy.core.crashlytics.Crashlytics

suspend fun <Key : Any, Value : Any> PagingSource<Key, Value>.execute(
    block: suspend () -> PagingSource.LoadResult<Key, Value>,
): PagingSource.LoadResult<Key, Value> {
    return kotlin.runCatching { block.invoke() }.getOrElse { error ->
        Crashlytics.record(error)
        PagingSource.LoadResult.Error(error)
    }
}