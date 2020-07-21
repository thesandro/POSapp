package com.possystem.posapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ProductEntry
import kotlinx.android.synthetic.main.parked_product_recyclerview_item_layout.view.*
import kotlinx.android.synthetic.main.product_recyclerview_item_layout.view.*

class ProductRecyclerViewAdapter(val items: ArrayList<ProductEntry>) :
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
            itemView.parkId.text = "გადადების ნომერი: " + model.parkId.toString()
            itemView.productPrice.text = "ფასი: " + (model.price*model.quantity).toString() + " ლარი"
            itemView.productQuantity.text = "რაოდენობა/წონა: " + model.quantity.toString()
        }
    }
}