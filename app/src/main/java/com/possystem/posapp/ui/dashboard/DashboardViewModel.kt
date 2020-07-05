package com.possystem.posapp.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.data.repository.PosRepository
import com.possystem.posapp.models.ProductSell

class DashboardViewModel(application: Application) :  AndroidViewModel(application) {

    private val repository = PosRepository.getRepositoryInstance(application)
    private val _parkedProducts = repository.getAllParkedProducts()
    val parkedProducts: LiveData<List<ProductEntry>> = _parkedProducts

}