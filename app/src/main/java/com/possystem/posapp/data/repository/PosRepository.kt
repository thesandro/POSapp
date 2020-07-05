package com.possystem.posapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.possystem.posapp.data.db.PosDataBase
import com.possystem.posapp.data.db.ProductDao
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.models.ProductSell
import com.possystem.posapp.network.DataLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PosRepository(application: Application) {
    private var productDao: ProductDao
    private var checkoutProducts: LiveData<List<ProductEntry>>
    private var parkedProducts: LiveData<List<ProductEntry>>

    companion object {
        @Volatile private var instance: PosRepository? = null
        fun getRepositoryInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: PosRepository(application).also { instance = it }
            }
    }
    init{
        val database: PosDataBase = PosDataBase.invoke(application.applicationContext)
        productDao = database.productDao()
        checkoutProducts = productDao.getCheckoutProducts()
        parkedProducts = productDao.getAllParkedProducts()
    }

    fun getAllCheckoutProducts():LiveData<List<ProductEntry>>{
        return checkoutProducts
    }
    fun getAllParkedProducts():LiveData<List<ProductEntry>>{
        return parkedProducts
    }
    suspend fun parkCheckoutProducts(){
        productDao.parkCheckoutProducts()
    }
    suspend fun fetchInsertProductService(map:Map<String,String>) {
        insertProductDB(DataLoader.getProduct(map))
    }
    suspend fun sellProducts(products:List<ProductSell>):String{
        return DataLoader.sellProducts(products)
    }

    private suspend fun insertProductDB(product:ProductEntry) {
        if(productDao.productExistsWithBarCode(product.barcode,-1))
            productDao.increaseProductQuantityBarCode(product.barcode,1.0,-1)
        else {
            product.quantity = 1.0
            product.parkId = -1
            productDao.upsert(product)
        }
    }
}