package com.pharmacy.common.ui.fragment.result

import android.os.Bundle
import android.os.Parcelable

interface FragmentResultHost<Value> {

    val defaultKey: String

    val packer: Packer<Value>


    interface Packer<Value> {

        fun boxing(result: Value): Bundle

        fun unboxing(data: Bundle): Value

    }


    open class ValueLong(override val defaultKey: String) : FragmentResultHost<Long> {
        override val packer: Packer<Long> get() = FragmentResultPackers.long
    }

    open class ValueInt(override val defaultKey: String) : FragmentResultHost<Int> {
        override val packer: Packer<Int> get() = FragmentResultPackers.integer
    }

    open class ValueFloat(override val defaultKey: String) : FragmentResultHost<Float> {
        override val packer: Packer<Float> get() = FragmentResultPackers.float
    }

    open class ValueBoolean(override val defaultKey: String) : FragmentResultHost<Boolean> {
        override val packer: Packer<Boolean> get() = FragmentResultPackers.boolean
    }

    open class ValueString(override val defaultKey: String) : FragmentResultHost<String> {
        override val packer: Packer<String> get() = FragmentResultPackers.string
    }

    open class ValueUnit(override val defaultKey: String) : FragmentResultHost<Unit> {
        override val packer: Packer<Unit> get() = FragmentResultPackers.unit
    }

    open class ValueTyped<T : Parcelable>(override val defaultKey: String) : FragmentResultHost<T> {
        override val packer: Packer<T> get() = FragmentResultPackers.getTypedParcelable()
    }

}