package com.possystem.posapp.ui.checkout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.possystem.posapp.App
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.models.ProductSell
import kotlinx.android.synthetic.main.fragment_checkout_sheet.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutSheetFragment(private val callerContext:Context,private val checkoutViewModel: CheckoutViewModel): BottomSheetDialogFragment() {
    private lateinit var rootView:View
    private val mutableArrayList = arrayListOf<ProductEntry>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_checkout_sheet, container, false)

        checkoutViewModel.products.observe(viewLifecycleOwner, Observer {
            mutableArrayList.clear()
            mutableArrayList.addAll(it)
            var totalSum = 0.0
            for (item in it){
                totalSum += item.price*item.quantity
            }
            rootView.totalSum.text = "ჯამური ფასი: " + totalSum.toString() + " ლარი"
        })
        rootView.sellButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(mutableArrayList.isNotEmpty()) {
                    val productList: MutableList<ProductSell> = arrayListOf()
                    for (item in mutableArrayList) {
                        productList.add(convertToSell(item))
                    }
                    val response = checkoutViewModel.sellProducts(productList)
                    Toast.makeText(
                        callerContext,
                        response.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    if (response.success) {
                        checkoutViewModel.clearCheckout()
                    }
                }
                else{
                    Toast.makeText(
                        callerContext,
                        App.instance.getContext().resources.getString(R.string.noProductsAdded),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dismiss()
        }

        rootView.checkoutCancelButton.setOnClickListener {
            dismiss()
        }
        return rootView
    }
    private fun convertToSell(productEntry: ProductEntry): ProductSell {
        return ProductSell(productEntry.barcode, productEntry.quantity, productEntry.measurement)
    }
}