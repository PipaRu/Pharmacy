package com.pharmacy.common.ui.fragment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner
import com.pharmacy.common.ui.fragment.result.FragmentResultController
import com.pharmacy.common.ui.fragment.result.FragmentResultHost
import com.pharmacy.common.ui.fragment.result.FragmentResultOwnerProvider

abstract class AppDialogFragment(contentLayoutId: Int = 0) : DialogFragment(contentLayoutId),
    FragmentResultOwnerProvider {

    override val fragmentResultOwner: FragmentResultOwner
        get() = requireActivity().supportFragmentManager


    fun <Result> FragmentResultHost<Result>.setResultListener(
        fragmentResultOwner: FragmentResultOwner = this@AppDialogFragment.fragmentResultOwner,
        lifecycleOwner: LifecycleOwner = this@AppDialogFragment,
        key: String = defaultKey,
        listener: (Result) -> Unit,
    ) {
        FragmentResultController.setResultListener(
            host = this,
            owner = fragmentResultOwner,
            lifecycleOwner = lifecycleOwner,
            key = key,
            listener = listener
        )
    }

    fun <Result> FragmentResultHost<Result>.setResult(
        fragmentResultOwner: FragmentResultOwner = this@AppDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
        result: Result,
    ) {
        FragmentResultController.setResult(
            host = this,
            owner = fragmentResultOwner,
            key = key,
            result = result
        )
    }

    fun FragmentResultHost<Unit>.trigger(
        fragmentResultOwner: FragmentResultOwner = this@AppDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
    ) {
        setResult(
            fragmentResultOwner = fragmentResultOwner,
            key = key,
            result = Unit
        )
    }

    fun <Result> FragmentResultHost<Result>.clearResult(
        fragmentResultOwner: FragmentResultOwner = this@AppDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
    ) {
        FragmentResultController.clearResult(
            host = this,
            owner = fragmentResultOwner,
            key = key
        )
    }

    fun <Result> FragmentResultHost<Result>.clearResultListener(
        fragmentResultOwner: FragmentResultOwner = this@AppDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
    ) {
        FragmentResultController.clearResultListener(
            host = this,
            owner = fragmentResultOwner,
            key = key
        )
    }

}