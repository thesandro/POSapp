package com.possystem.posapp.ui.notifications

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
import com.possystem.posapp.ui.adapters.ProductRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_products.view.*

class ProductsFragment : Fragment() {

    private lateinit var notificationsViewModel: ProductsViewModel
    private lateinit var adapter:ProductRecyclerViewAdapter
    private val mutableArrayList = arrayListOf<ProductEntry>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(ProductsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        adapter = ProductRecyclerViewAdapter(mutableArrayList)
        root.productsRecyclerView.adapter = adapter
        root.productsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        notificationsViewModel.products.observe(viewLifecycleOwner, Observer {
            mutableArrayList.clear()
            mutableArrayList.addAll(it)
            adapter.notifyDataSetChanged()

        })
        return root
    }

}