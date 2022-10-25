package com.pharmacy.common.filter

object Filters {

    fun <T> disabled(): Filter<T> = DisabledFilter

    fun <T> blocker(): Filter<T> = BlockerFilter

    fun <T> create(filter: (T) -> Boolean): Filter<T> = Filter(filter::invoke)


    private object DisabledFilter : Filter<Any?> {
        override fun filter(value: Any?): Boolean = true
    }

    private object BlockerFilter : Filter<Any?> {
        override fun filter(value: Any?): Boolean = false
    }

}