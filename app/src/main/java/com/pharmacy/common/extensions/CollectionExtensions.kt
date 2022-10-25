package com.pharmacy.common.extensions

import com.pharmacy.common.filter.Filter
import kotlin.collections.filter as collectionFilter

fun <C : Collection<T>, T> C.filter(filter: Filter<T>) = collectionFilter(filter::filter)

fun <C : Collection<T>, T> C.updateElement(
    element: T,
    compare: (current: T, update: T) -> Boolean
): List<T> {
    return map { item ->
        if (compare.invoke(item, element)) {
            element
        } else {
            item
        }
    }
}

fun <C : Collection<T>, T> C.updateElement(
    element: T,
    identifier: (T) -> Any?
): List<T> {
    return updateElement(element) { current, update ->
        identifier.invoke(current) == identifier.invoke(update)
    }
}

fun <C : Collection<T>, T> C.updateElements(
    compare: (current: T, update: T) -> Boolean,
    vararg elements: T
): List<T> {
    return updateElements(
        elements = elements.toList(),
        compare = compare
    )
}

fun <C : Collection<T>, T> C.updateElements(
    elements: Collection<T>,
    compare: (current: T, update: T) -> Boolean
): List<T> {
    if (elements.isEmpty()) return toList()
    return map { item -> elements.firstOrNull { update -> compare.invoke(item, update) } ?: item }
}