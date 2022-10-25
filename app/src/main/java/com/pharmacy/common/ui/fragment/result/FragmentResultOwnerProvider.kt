package com.pharmacy.common.ui.fragment.result

import androidx.fragment.app.FragmentResultOwner

interface FragmentResultOwnerProvider {

    val fragmentResultOwner: FragmentResultOwner

    companion object {
        val Empty: FragmentResultOwnerProvider = object : FragmentResultOwnerProvider {
            override val fragmentResultOwner: FragmentResultOwner = EmptyFragmentResultOwner
        }
    }

}