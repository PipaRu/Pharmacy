package com.pharmacy.common.ui.fragment.result

import android.os.Bundle
import android.os.Parcelable

object FragmentResultPackers {

    private const val KEY_RESULT_VALUE = "value"

    val unit: FragmentResultHost.Packer<Unit> = object : FragmentResultHost.Packer<Unit> {
        override fun boxing(result: Unit): Bundle = Bundle.EMPTY
        override fun unboxing(data: Bundle): Unit = Unit
    }

    val integer: FragmentResultHost.Packer<Int> = object : FragmentResultHost.Packer<Int> {
        override fun boxing(result: Int): Bundle {
            return internalBoxing(result) { key, value -> putInt(key, value) }
        }
        override fun unboxing(data: Bundle): Int {
            return internalUnboxing(data) { key -> getInt(key) }
        }
    }

    val long: FragmentResultHost.Packer<Long> = object : FragmentResultHost.Packer<Long> {
        override fun boxing(result: Long): Bundle {
            return internalBoxing(result) { key, value -> putLong(key, value) }
        }
        override fun unboxing(data: Bundle): Long {
            return internalUnboxing(data) { key -> getLong(key) }
        }
    }

    val float: FragmentResultHost.Packer<Float> = object : FragmentResultHost.Packer<Float> {
        override fun boxing(result: Float): Bundle {
            return internalBoxing(result) { key, value -> putFloat(key, value) }
        }
        override fun unboxing(data: Bundle): Float {
            return internalUnboxing(data) { key -> getFloat(key) }
        }
    }

    val string: FragmentResultHost.Packer<String> = object : FragmentResultHost.Packer<String> {
        override fun boxing(result: String): Bundle {
            return internalBoxing(result) { key, value -> putString(key, value) }
        }
        override fun unboxing(data: Bundle): String {
            return internalUnboxing(data) { key -> requireNotNull(getString(key)) }
        }
    }

    val boolean: FragmentResultHost.Packer<Boolean> = object : FragmentResultHost.Packer<Boolean> {
        override fun boxing(result: Boolean): Bundle {
            return internalBoxing(result) { key, value -> putBoolean(key, value) }
        }
        override fun unboxing(data: Bundle): Boolean {
            return internalUnboxing(data) { key -> getBoolean(key) }
        }
    }

    fun <T : Parcelable> getTypedParcelable(): FragmentResultHost.Packer<T> {
        return object : FragmentResultHost.Packer<T> {
            override fun boxing(result: T): Bundle {
                return internalBoxing(result) { key, value -> putParcelable(key, value) }
            }
            override fun unboxing(data: Bundle): T {
                return internalUnboxing(data) { key -> getParcelable<T>(key) as T }
            }
        }
    }

    private fun <T> internalBoxing(result: T, block: Bundle.(key: String, value: T) -> Unit): Bundle {
        return Bundle(1).apply { block.invoke(this, KEY_RESULT_VALUE, result) }
    }

    private fun <T> internalUnboxing(data: Bundle, block: Bundle.(key: String) -> T): T {
        return with(data) {
            requireKeyContaining()
            block(KEY_RESULT_VALUE)
        }
    }

    private fun Bundle.requireKeyContaining() {
        require(containsKey(KEY_RESULT_VALUE)) {
            "Bundle does not contain a value"
        }
    }

}