package com.pharmacy.core.component

import android.app.Application

interface Component {

    fun init(application: Application)

    companion object {
        fun Application.initializeComponents(vararg components: Component) {
            initializeComponents(components.toSet())
        }
        fun Application.initializeComponents(components: Set<Component>) {
            components.forEach { it.init(this) }
        }
    }
}