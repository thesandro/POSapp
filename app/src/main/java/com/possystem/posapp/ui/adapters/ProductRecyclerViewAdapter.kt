package com.possystem.posapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.ui.checkout.CheckoutViewModel
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import kotlinx.android.synthetic.main.parked_product_recyclerview_item_layout.view.*
import kotlinx.android.synthetic.main.product_recyclerview_item_layout.view.*

class ProductRecyclerViewAdapter(val items: ArrayList<ProductEntry>, val checkoutViewModel: CheckoutViewModel, val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_recyclerview_item_layout, parent, false
        )
    )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItems()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private lateinit var model: ProductEntry

        fun setItems(){
            model = items[adapterPosition]
            itemView.productName.text = "პროდუქტი: " + model.name
            itemView.productPrice.text = "ფასი: " + (model.price*model.quantity).toString() + " ლარი"
            itemView.productQuantity.text = "რაოდენობა/წონა: " + model.quantity.toString()
            initPlusMinus()
        }
        private fun initPlusMinus(){
            itemView.integer_number.setText(model.quantity.toString())
            itemView.decrease.setOnClickListener {
                if(itemView.integer_number.text.toString().toDouble() > 1.0){
                    checkoutViewModel.changeProductValue(model.barcode,-1)
                }
            }
            itemView.increase.setOnClickListener {
                checkoutViewModel.changeProductValue(model.barcode,1)
            }
        }
    }
}