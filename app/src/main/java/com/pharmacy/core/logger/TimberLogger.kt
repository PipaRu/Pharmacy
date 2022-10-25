package com.pharmacy.core.logger

import timber.log.Timber

class TimberLogger : Logger {

    init {
        initTimber()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initTimber() {
        // TODO: Release solution
        val debugTree = Timber.DebugTree()
        val declared = debugTree.javaClass.getDeclaredField("fqcnIgnore")
        declared.isAccessible = true
        val value = declared.get(debugTree) as List<String>
        val newList = ArrayList(value).apply {
            add(Logger::class.java.name)
            add(Logger.Companion::class.java.name)
            add(TimberLogger::class.java.name)
            add("com.pharmacy.core.logger.Logger\$DefaultImpls")
        }
        declared.set(debugTree, newList)
        Timber.plant(debugTree)
    }

    override fun verbose(message: String?, throwable: Throwable?) {
        Timber.v(throwable, message)
    }

    override fun error(message: String?, throwable: Throwable?) {
        Timber.e(throwable, message)
    }

    override fun warning(message: String?, throwable: Throwable?) {
        Timber.w(throwable, message)
    }

    override fun debug(message: String?, throwable: Throwable?) {
        Timber.d(throwable, message)
    }

    override fun info(message: String?, throwable: Throwable?) {
        Timber.i(throwable, message)
    }

}