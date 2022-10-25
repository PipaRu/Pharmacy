package com.pharmacy.common.ui.fragment.result

import android.os.Bundle
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner

object EmptyFragmentResultOwner : FragmentResultOwner {
    override fun setFragmentResult(requestKey: String, result: Bundle) {}
    override fun clearFragmentResult(requestKey: String) {}
    override fun setFragmentResultListener(requestKey: String, lifecycleOwner: LifecycleOwner, listener: FragmentResultListener, ) {}
    override fun clearFragmentResultListener(requestKey: String) {}
}