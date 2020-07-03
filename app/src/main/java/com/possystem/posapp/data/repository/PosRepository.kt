package com.possystem.posapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.possystem.posapp.data.db.PosDataBase
import com.possystem.posapp.data.db.ProductDao
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.network.DataLoader.getProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PosRepository(application: Application) {
    private var productDao: ProductDao
    private lateinit var product: LiveData<List<ProductEntry>>

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PosRepository? = null

        fun getRepositoryInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: PosRepository(application).also { instance = it }
            }
    }
    init{
        val database: PosDataBase = PosDataBase.invoke(application.applicationContext)
        productDao = database.productDao()
        CoroutineScope(Dispatchers.IO).launch {
            product = productDao.getAllProducts()
        }
    }
    suspend fun fetchInsertProductService(map:Map<String,String>) {
        insertProductDB(getProduct(map))
    }
    private suspend fun insertProductDB(product:ProductEntry) {
        if(productDao.productExistsWithBarCode(product.barcode))
            productDao.increaseProductQuantityBarCode(product.barcode,1.0)
        else
            productDao.upsert(product)
    }

    fun getAllProducts():LiveData<List<ProductEntry>>{
        return product
    }
}