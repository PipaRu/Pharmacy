package com.pharmacy.ui.dialog.address_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.pharmacy.common.ui.fragment.AppBottomSheetDialogFragment
import com.pharmacy.common.ui.fragment.result.FragmentResultHost
import com.pharmacy.ui.dialog.address_picker.content.AddressPickerScreenContent
import com.pharmacy.ui.dialog.address_picker.model.mvi.AddressPickerSideEffect
import com.pharmacy.ui.model.AddressItem
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.orbitmvi.orbit.viewmodel.observe

class AddressPickerDialog : AppBottomSheetDialogFragment() {

    private val viewModel by stateViewModel<AddressPickerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AddressPickerScreenContent(
                    viewModel
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(
            lifecycleOwner = viewLifecycleOwner,
            state = null,
            sideEffect = ::handleSideEffect
        )
    }

    private fun handleSideEffect(effect: AddressPickerSideEffect): Unit = when (effect) {
        is AddressPickerSideEffect.AddressSelected -> sendSelectedAddressAndClose(effect.address)
    }

    private fun sendSelectedAddressAndClose(address: AddressItem) {
        setResult(address)
        dismiss()
    }

    companion object : FragmentResultHost.ValueTyped<AddressItem>(
        defaultKey = "address_picker_dialog_request_key"
    )

}