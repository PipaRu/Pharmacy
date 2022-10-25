package com.pharmacy.common.ui.fragment

import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pharmacy.common.ui.fragment.result.FragmentResultController
import com.pharmacy.common.ui.fragment.result.FragmentResultHost
import com.pharmacy.common.ui.fragment.result.FragmentResultOwnerProvider

abstract class AppBottomSheetDialogFragment : BottomSheetDialogFragment(),
    FragmentResultOwnerProvider {

    override val fragmentResultOwner: FragmentResultOwner
        get() = requireActivity().supportFragmentManager


    fun <Result> FragmentResultHost<Result>.setResultListener(
        fragmentResultOwner: FragmentResultOwner = this@AppBottomSheetDialogFragment.fragmentResultOwner,
        lifecycleOwner: LifecycleOwner = this@AppBottomSheetDialogFragment,
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
        result: Result,
        fragmentResultOwner: FragmentResultOwner = this@AppBottomSheetDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
    ) {
        FragmentResultController.setResult(
            host = this,
            owner = fragmentResultOwner,
            key = key,
            result = result
        )
    }

    fun FragmentResultHost<Unit>.trigger(
        fragmentResultOwner: FragmentResultOwner = this@AppBottomSheetDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
    ) {
        setResult(
            fragmentResultOwner = fragmentResultOwner,
            key = key,
            result = Unit
        )
    }

    fun <Result> FragmentResultHost<Result>.clearResult(
        fragmentResultOwner: FragmentResultOwner = this@AppBottomSheetDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
    ) {
        FragmentResultController.clearResult(
            host = this,
            owner = fragmentResultOwner,
            key = key
        )
    }

    fun <Result> FragmentResultHost<Result>.clearResultListener(
        fragmentResultOwner: FragmentResultOwner = this@AppBottomSheetDialogFragment.fragmentResultOwner,
        key: String = defaultKey,
    ) {
        FragmentResultController.clearResultListener(
            host = this,
            owner = fragmentResultOwner,
            key = key
        )
    }

}