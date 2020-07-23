package com.possystem.posapp.ui.checkout

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.possystem.posapp.App
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.models.ProductSell
import com.possystem.posapp.ui.adapters.ProductRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var adapter: ProductRecyclerViewAdapter
    private val mutableArrayList = arrayListOf<ProductEntry>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkoutViewModel =
            ViewModelProvider(this).get(CheckoutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_checkout, container, false)
        adapter = ProductRecyclerViewAdapter(mutableArrayList,checkoutViewModel,viewLifecycleOwner)
        root.productsRecyclerView.adapter = adapter
        root.productsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        checkoutViewModel.products.observe(viewLifecycleOwner, Observer {
            mutableArrayList.clear()
            mutableArrayList.addAll(it)
            adapter.notifyDataSetChanged()
            if (it.isEmpty())
                root.checkoutEmptyTV.visibility = View.VISIBLE
            else
                root.checkoutEmptyTV.visibility = View.GONE
        })
        root.sellButton.setOnClickListener {
            showBottomSellDialog()
        }
        root.parkButton.setOnClickListener {
            showBottomSheetDialog()
        }
        return root
    }
    private fun showBottomSheetDialog() {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        val prev = activity?.supportFragmentManager?.findFragmentByTag("dialog")
        if (prev != null) {
            fragmentTransaction?.remove(prev)
        }
        val dialogFragment = ParkSheetFragment(checkoutViewModel)
        fragmentTransaction?.addToBackStack(dialogFragment.tag)
        dialogFragment.show(activity?.supportFragmentManager!!,"dialog")
    }
    private fun showBottomSellDialog(){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        val prev = activity?.supportFragmentManager?.findFragmentByTag("dialogsell")
        if (prev != null) {
            fragmentTransaction?.remove(prev)
        }
        val dialogFragment = CheckoutSheetFragment(this.requireContext(),checkoutViewModel)
        fragmentTransaction?.addToBackStack(dialogFragment.tag)
        dialogFragment.show(activity?.supportFragmentManager!!,"dialogsell")
    }



}