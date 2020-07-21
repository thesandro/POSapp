package com.possystem.posapp.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.possystem.posapp.R
import kotlinx.android.synthetic.main.fragment_park_sheet.view.*

class ParkSheetFragment(private val checkoutViewModel: CheckoutViewModel): BottomSheetDialogFragment() {
    private lateinit var rootView:View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_park_sheet, container, false)
        rootView.parkButton.setOnClickListener {
            checkoutViewModel.parkProducts(rootView.nameEditText.text.toString(),rootView.noteEditText.text.toString())
            dismiss()
        }
        rootView.cancelButton.setOnClickListener {
            dismiss()
        }
        return rootView
    }
}