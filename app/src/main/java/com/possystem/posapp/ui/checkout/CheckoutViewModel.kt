package com.possystem.posapp.ui.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.possystem.posapp.data.db.ParkedProductEntry
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.data.repository.PosRepository
import com.possystem.posapp.models.ProductSell
import com.possystem.posapp.network.model.ResponseMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PosRepository.getRepositoryInstance(application)
    private val _products = repository.getAllCheckoutProducts()

    val products: LiveData<List<ProductEntry>> = _products

    suspend fun sellProducts(products:List<ProductSell>):ResponseMessage {
        return repository.sellProducts(products)
    }

    fun parkProducts(fullName:String,note:String){
        CoroutineScope(Dispatchers.IO).launch {
            val parkedProductEntry = ParkedProductEntry(fullName = fullName, note = note, parkTime = Calendar.getInstance().timeInMillis)
            repository.parkCheckoutProducts(parkedProductEntry)
        }
    }

    fun clearCheckout(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteParkedProducts(0)
        }
    }

    fun changeProductValue(barcode:String,value:Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.changeProductQuantity(barcode,value)
        }
    }
}