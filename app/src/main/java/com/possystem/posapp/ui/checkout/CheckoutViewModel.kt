package com.possystem.posapp.ui.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.data.repository.PosRepository
import com.possystem.posapp.models.ProductSell

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PosRepository.getRepositoryInstance(application)
    private val _products = repository.getAllCheckoutProducts()

    val products: LiveData<List<ProductEntry>> = _products

    suspend fun sellProducts(products:List<ProductSell>):String {
        return repository.sellProducts(products)
    }
    suspend fun parkProducts(){
        repository.parkCheckoutProducts()
    }
}