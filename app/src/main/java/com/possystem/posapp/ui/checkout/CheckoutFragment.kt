package com.possystem.posapp.ui.checkout

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var adapter:ProductRecyclerViewAdapter
    private val mutableArrayList = arrayListOf<ProductEntry>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        checkoutViewModel =
                ViewModelProvider(this).get(CheckoutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_checkout, container, false)
        adapter = ProductRecyclerViewAdapter(mutableArrayList)
        root.productsRecyclerView.adapter = adapter
        root.productsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        checkoutViewModel.products.observe(viewLifecycleOwner, Observer {
            mutableArrayList.clear()
            mutableArrayList.addAll(it)
            adapter.notifyDataSetChanged()
            if(it.isEmpty())
                root.checkoutEmptyTV.visibility = View.VISIBLE
            else
                root.checkoutEmptyTV.visibility = View.GONE
        })
        root.sellButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val productList:MutableList<ProductSell> = arrayListOf()
                for(item in mutableArrayList){
                    productList.add(convertToSell(item))
                }
                val response = checkoutViewModel.sellProducts(productList)
                d("loglogdato",response)
            }
        }
        root.parkButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                checkoutViewModel.parkProducts()
            }
        }
        return root
    }

    private fun convertToSell(productEntry: ProductEntry):ProductSell{
        return ProductSell(productEntry.barcode,productEntry.quantity,productEntry.measurement)
    }

}