package com.possystem.posapp.data.db


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName



@Entity(tableName = "products")
data class ProductEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @SerializedName("park_id")
    var parkId:Int,
    @SerializedName("barcode")
    val barcode:String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    var quantity:Double,
    @SerializedName("measurement")
    val measurement: String
)