package com.pharmacy.common.paging

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class PagingList<T : Any> {

    private val _items = MutableStateFlow(
        ItemSnapshotList<T>(
            placeholdersBefore = 0,
            placeholdersAfter = 0,
            items = emptyList()
        )
    )

    val items: Flow<List<T?>>
        get() = _items.asStateFlow()

    val itemCount: Int
        get() = _items.value.size

    private val differCallback: DifferCallback = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {
            if (count > 0) updateItemSnapshotList()
        }

        override fun onInserted(position: Int, count: Int) {
            if (count > 0) updateItemSnapshotList()
        }

        override fun onRemoved(position: Int, count: Int) {
            if (count > 0) updateItemSnapshotList()
        }
    }

    private val pagingDataDiffer = object : PagingDataDiffer<T>(
        differCallback = differCallback,
        mainContext = Dispatchers.Main
    ) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit,
        ): Int? {
            onListPresentable()
            updateItemSnapshotList()
            return null
        }
    }

    private fun updateItemSnapshotList() {
        _items.value = pagingDataDiffer.snapshot()
    }

    fun get(index: Int): T? {
        val correctedIndex = index.coerceIn(0 until pagingDataDiffer.size)
        pagingDataDiffer[correctedIndex]
        return _items.value[correctedIndex]
    }

    fun peek(index: Int): T? {
        return _items.value[index]
    }

    fun retry() {
        pagingDataDiffer.retry()
    }

    fun refresh() {
        pagingDataDiffer.refresh()
    }

    val loadState: Flow<CombinedLoadStates>
        get() = pagingDataDiffer.loadStateFlow

    suspend fun collectPagingData(data: Flow<PagingData<T>>) {
        data.collectLatest { pagingDataDiffer.collectFrom(it) }
    }

}