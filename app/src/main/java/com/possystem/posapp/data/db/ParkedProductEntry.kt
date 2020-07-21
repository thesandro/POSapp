package com.possystem.posapp.data.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "parkedProducts")
class ParkedProductEntry (
    @SerializedName("full_name")
    val fullName:String,
    @SerializedName("note")
    val note:String,
    @SerializedName("park_time")
    val parkTime: Long
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @Ignore
    var count:Int = 0
}