package com.possystem.posapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.possystem.posapp.network.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: ProductEntry)

    @Query("select EXISTS(select * from products where barcode = :barcode LIMIT 1)")
    suspend fun productExistsWithBarCode(barcode:String):Boolean

    @Query("update products set quantity = quantity + :quantity  where barcode == :barcode")
    suspend fun increaseProductQuantityBarCode(barcode: String, quantity:Double)

    @Query("select * from products where id = :productId LIMIT 1")
    fun getProduct(productId:Int): LiveData<ProductEntry>


    @Query("select * from products")
    fun getAllProducts(): LiveData<List<ProductEntry>>

}