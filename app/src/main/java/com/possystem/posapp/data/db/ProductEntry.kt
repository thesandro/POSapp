package com.possystem.posapp.data.db


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName



@Entity(tableName = "products")
data class ProductEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @SerializedName("barcode")
    val barcode:String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    val quantity:Double,
    @SerializedName("measurement")
    val measurement: String
)