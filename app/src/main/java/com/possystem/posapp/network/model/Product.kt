package com.possystem.posapp.network.model

import com.google.gson.annotations.SerializedName

class Product(
    @SerializedName("product_id")
    val productId:Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("price")
    val price: Double
)