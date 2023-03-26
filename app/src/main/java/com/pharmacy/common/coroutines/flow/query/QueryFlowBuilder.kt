@file:OptIn(FlowPreview::class)

package com.pharmacy.common.coroutines.flow.query

import com.pharmacy.common.extensions.Empty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun QueryFlow(
    debounce: Duration,
    coroutineScope: CoroutineScope,
): QueryFlow = BaseQueryFlow(
    debounce = debounce,
    coroutineScope = coroutineScope
)

fun QueryFlow(
    debounceMilliseconds: Long,
    coroutineScope: CoroutineScope,
    initialQuery: String = String.Empty,
): QueryFlow = BaseQueryFlow(
    debounce = debounceMilliseconds.toDuration(DurationUnit.MILLISECONDS),
    coroutineScope = coroutineScope,
    initialQuery = initialQuery,
)

private class BaseQueryFlow(
    debounce: Duration,
    coroutineScope: CoroutineScope,
    initialQuery: String = String.Empty,
) : QueryFlow {

    private val executableQuery: MutableStateFlow<String> = MutableStateFlow(initialQuery)

    private val waitingQuery: MutableStateFlow<String> = MutableStateFlow(initialQuery)

    init {
        waitingQuery
            .debounce(debounce)
            .onEach(this::execute)
            .launchIn(coroutineScope)
    }

    override val pending: StateFlow<String>
        get() = waitingQuery.asStateFlow()

    override val value: String
        get() = executableQuery.value

    override suspend fun query(value: String) {
        waitingQuery.emit(value)
    }

    override suspend fun execute(query: String) {
        executableQuery.emit(query)
    }

    override val replayCache: List<String>
        get() = executableQuery.replayCache

    override suspend fun collect(collector: FlowCollector<String>): Nothing {
        executableQuery.collect(collector)
    }

}