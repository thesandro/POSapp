package com.possystem.posapp.data.repository

import android.app.Application
import android.util.Log.d
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.possystem.posapp.App
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ParkedProductEntry
import com.possystem.posapp.data.db.PosDataBase
import com.possystem.posapp.data.db.ProductDao
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.data.user_preference.UserPreference
import com.possystem.posapp.models.ProductSell
import com.possystem.posapp.network.DataLoader
import com.possystem.posapp.network.Endpoints
import com.possystem.posapp.network.model.ResponseMessage
import com.possystem.posapp.ui.activities.authentication.log_in.LogInModel
import java.lang.Exception

class PosRepository(application: Application) {
    private var productDao: ProductDao
    private var checkoutProducts: LiveData<List<ProductEntry>>
    private var parkedProducts: LiveData<List<ParkedProductEntry>>

    companion object {
        @Volatile
        private var instance: PosRepository? = null
        fun getRepositoryInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: PosRepository(application).also { instance = it }
            }
    }

    init {
        val database: PosDataBase = PosDataBase.invoke(application.applicationContext)
        productDao = database.productDao()
        checkoutProducts = productDao.getCheckoutProducts()
        parkedProducts = productDao.getAllParked()
    }

    fun getAllCheckoutProducts(): LiveData<List<ProductEntry>> {
        return checkoutProducts
    }

    fun getAllParked(): LiveData<List<ParkedProductEntry>> {
        return parkedProducts
    }

    suspend fun getProductCount(parkId: Int) = productDao.getParkedProductCount(parkId)

    suspend fun parkCheckoutProducts(parkedProductEntry: ParkedProductEntry) {
        val parkId = productDao.upsertParked(parkedProductEntry)
        productDao.parkCheckoutProducts(parkId.toInt())
    }

    suspend fun logIn(username:String,password:String):Any{
        return try {
            val form = mapOf(
                "pin" to username,
                "password" to password
            )
            val loginResponse = DataLoader.postForm(Endpoints.LOG_IN,form)
            return if (loginResponse.code() == DataLoader.HTTP_200_OK && loginResponse.body() != null) {
                 Gson().fromJson(loginResponse.body()!!, LogInModel::class.java)
            } else {
                ResponseMessage(
                    false,
                    DataLoader.handleApiError(loginResponse.code(), loginResponse.errorBody())
                )
            }
        } catch (ex: Exception) {
            d("loglogex", ex.message.toString())
            ResponseMessage(
                false,
                App.instance.getContext().resources.getString(R.string.service_unavailable)
            )
        }
    }

    suspend fun fetchInsertProductService(map: Map<String, String>): ResponseMessage {
        return try {
            val productResponse = DataLoader.getProduct(map)
            return if (productResponse.code() == DataLoader.HTTP_200_OK && productResponse.body() != null) {
                insertProductDB(productResponse.body()!!)
                ResponseMessage(true, productResponse.body()!!.name + " დამატდა")
            } else {
                ResponseMessage(
                    false,
                    DataLoader.handleApiError(productResponse.code(), productResponse.errorBody())
                )
            }
        } catch (ex: Exception) {
            d("loglogex", ex.message.toString())
            ResponseMessage(
                false,
                App.instance.getContext().resources.getString(R.string.service_unavailable)
            )
        }
    }

    suspend fun sellProducts(products: List<ProductSell>): ResponseMessage {
        val cashierPin = UserPreference.getString(UserPreference.USER_ID)!!
        return try {
            val sellResponse = DataLoader.sellProducts(cashierPin, products)
            if (sellResponse.code() == DataLoader.HTTP_200_OK && sellResponse.body() != null) {
                ResponseMessage(true, "გაიყიდა!")
            } else {
                ResponseMessage(
                    false,
                    DataLoader.handleApiError(sellResponse.code(), sellResponse.errorBody())
                )
            }
        } catch (ex: Exception) {
            ResponseMessage(
                false,
                App.instance.getContext().resources.getString(R.string.service_unavailable)
            )
        }
    }

    suspend fun deleteParkedProducts(parkId: Int) {
        productDao.deleteParkedProducts(parkId)
        productDao.deleteParked(parkId)
    }
    suspend fun checkoutParkedProducts(parkId: Int){
        productDao.checkoutParkedProducts(parkId)
        productDao.deleteParked(parkId)
    }

    private suspend fun insertProductDB(product: ProductEntry) {
        if (productDao.productExistsWithBarCode(product.barcode, 0)) {
            productDao.increaseProductQuantityBarCode(product.barcode, 1.0, 0)
        } else {
            product.quantity = 1.0
            product.parkId = 0
            productDao.upsert(product)
        }
    }
    suspend fun changeProductQuantity(barcode:String,value:Int){
        productDao.increaseProductQuantityBarCode(barcode, value.toDouble(), 0)
    }
}