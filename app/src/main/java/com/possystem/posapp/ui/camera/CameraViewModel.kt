package com.possystem.posapp.ui.camera

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.possystem.posapp.data.repository.PosRepository.Companion.getRepositoryInstance
import com.possystem.posapp.network.model.ResponseMessage

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getRepositoryInstance(application)
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    suspend fun insert(map:Map<String,String>):ResponseMessage {
        return repository.fetchInsertProductService(map)
    }
}