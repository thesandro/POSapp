package com.possystem.posapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.ui.adapters.ProductRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var adapter:ProductRecyclerViewAdapter
    private val mutableArrayList = arrayListOf<ProductEntry>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        adapter = ProductRecyclerViewAdapter(mutableArrayList)
        root.parkedProductsRecyclerView.adapter = adapter
        root.parkedProductsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        dashboardViewModel.parkedProducts.observe(viewLifecycleOwner, Observer {
            mutableArrayList.clear()
            mutableArrayList.addAll(it)
            adapter.notifyDataSetChanged()
            if(it.isEmpty())
                root.parkEmptyTV.visibility = View.VISIBLE
            else
                root.parkEmptyTV.visibility = View.GONE

        })
        return root
    }
}