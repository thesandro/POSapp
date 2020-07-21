package com.possystem.posapp.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.possystem.posapp.data.db.ParkedProductEntry
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.data.repository.PosRepository
import com.possystem.posapp.models.ProductSell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) :  AndroidViewModel(application) {

    private val repository = PosRepository.getRepositoryInstance(application)
    private val _parkedProducts = repository.getAllParked()
    val parkedProducts: LiveData<List<ParkedProductEntry>> = _parkedProducts

    suspend fun getProductCount(parkId:Int) = repository.getProductCount(parkId)

    fun parkToCheckout(parkId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.checkoutParkedProducts(parkId)
        }
    }
    fun deleteParkedProduct(parkId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteParkedProducts(parkId)
        }
    }
}