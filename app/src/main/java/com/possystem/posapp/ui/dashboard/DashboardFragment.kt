package com.possystem.posapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ParkedProductEntry
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.ui.adapters.ParkedProductRecyclerViewAdapter
import com.possystem.posapp.ui.adapters.ProductRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var adapter:ParkedProductRecyclerViewAdapter
    private val mutableArrayList = arrayListOf<ParkedProductEntry>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        adapter = ParkedProductRecyclerViewAdapter(mutableArrayList,this.requireContext(),dashboardViewModel)
        root.parkedProductsRecyclerView.adapter = adapter
        root.parkedProductsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        dashboardViewModel.parkedProducts.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                for(item in it){
                    item.count = dashboardViewModel.getProductCount(item.id)
                }

                mutableArrayList.clear()
                mutableArrayList.addAll(it)
                adapter.notifyDataSetChanged()
                if(it.isEmpty())
                    root.parkEmptyTV.visibility = View.VISIBLE
                else
                    root.parkEmptyTV.visibility = View.GONE
            }

        })
        return root
    }
}