package com.possystem.posapp.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.data.repository.PosRepository
import com.possystem.posapp.network.model.Product

class ProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PosRepository.getRepositoryInstance(application)
    private val _products = repository.getAllProducts()

    val products: LiveData<List<ProductEntry>> = _products
}