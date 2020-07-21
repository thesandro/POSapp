package com.possystem.posapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.possystem.posapp.network.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsert(product: ProductEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertParked(product: ParkedProductEntry):Long


    @Query("select EXISTS(select * from products where barcode == :barcode and parkId == :parkId LIMIT 1)")
    suspend fun productExistsWithBarCode(barcode:String,parkId:Int):Boolean

    @Query("select EXISTS(select * from parkedProducts)")
    suspend fun checkoutExists():Boolean

    @Query("update products set parkId = :parkId where parkId == 0")
    suspend fun parkCheckoutProducts(parkId:Int)

    @Query("update products set quantity = quantity + :quantity  where barcode == :barcode and parkId == :parkId")
    suspend fun increaseProductQuantityBarCode(barcode: String, quantity:Double,parkId: Int)

    @Query("delete from products where parkId == :parkId")
    suspend fun deleteParkedProducts(parkId: Int)

    @Query("delete from parkedProducts where id == :parkId")
    suspend fun deleteParked(parkId: Int)

    @Query("select * from products where id = :productId LIMIT 1")
    fun getProduct(productId:Int): LiveData<ProductEntry>

    @Query("select * from products")
    fun getAllProducts(): LiveData<List<ProductEntry>>

    @Query("select * from products where parkId == 0")
    fun getCheckoutProducts():LiveData<List<ProductEntry>>

    @Query("select * from products where parkId != 0")
    fun getAllParkedProducts(): LiveData<List<ProductEntry>>

    @Query("select * from parkedProducts where id != 0")
    fun getAllParked():LiveData<List<ParkedProductEntry>>

    @Query("select COUNT(*) from products where parkId = :parkId")
    suspend fun getParkedProductCount(parkId: Int):Int

    @Query("update products set parkId = 0 where parkId == :parkId")
    suspend fun checkoutParkedProducts(parkId:Int)

}