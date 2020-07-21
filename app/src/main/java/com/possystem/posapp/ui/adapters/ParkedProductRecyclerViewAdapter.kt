package com.possystem.posapp.ui.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.icu.text.RelativeDateTimeFormatter
import android.text.format.DateUtils
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.possystem.posapp.App
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ParkedProductEntry
import com.possystem.posapp.ui.dashboard.DashboardViewModel
import kotlinx.android.synthetic.main.error_dialog_layout.*
import kotlinx.android.synthetic.main.parked_product_recyclerview_item_layout.view.*


class ParkedProductRecyclerViewAdapter(val parkedProducts: ArrayList<ParkedProductEntry>,val context: Context,val dashboardViewModel: DashboardViewModel) : RecyclerView.Adapter<ParkedProductRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.parked_product_recyclerview_item_layout, parent, false
        )
    )

    override fun getItemCount(): Int = parkedProducts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItems()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private lateinit var model: ParkedProductEntry

        fun setItems(){
            model = parkedProducts[adapterPosition]
            itemView.clientName.text = "სახელი: " + model.fullName
            itemView.parkNote.text = "შენიშვნა: " + model.note

            val getRelativeString = DateUtils.getRelativeDateTimeString(
                App.instance.getContext(), model.parkTime,DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0)
            itemView.timePassed.text = "დრო: " + getRelativeString
            itemView.productCount.text = "რაოდენობა: " + model.count
            itemView.setOnClickListener {
                parkDialog(context, App.instance.getContext().getString(R.string.park_dialog_title),App.instance.getContext().getString(R.string.park_dialog_description))
            }
        }

        private fun parkDialog(context: Context, title: String, description: String) {
            val dialog = Dialog(context)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.error_dialog_layout)

            val params: ViewGroup.LayoutParams = dialog.window!!.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = params as WindowManager.LayoutParams
            dialog.titleTV.text = title
            dialog.descriptionTV.text = description
            dialog.noButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.deleteButton.setOnClickListener{
                dashboardViewModel.deleteParkedProduct(model.id)
                dialog.dismiss()
            }
            dialog.okButton.setOnClickListener {
                dashboardViewModel.parkToCheckout(model.id)
                dialog.dismiss()
            }

            dialog.show()

        }
    }
}