package com.possystem.posapp.ui.activities.authentication.log_in

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.possystem.posapp.data.repository.PosRepository
import com.possystem.posapp.data.user_preference.UserPreference
import com.possystem.posapp.network.model.ResponseMessage

class LogInViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PosRepository.getRepositoryInstance(application)

    suspend fun logIn(userName:String,password:String,rememberMe:Boolean): ResponseMessage {
        return when(val responseMessage = repository.logIn(userName,password)){
            is LogInModel -> {
                saveInstance(responseMessage,rememberMe)
                ResponseMessage(true,"Logged in")
            }
            is ResponseMessage -> responseMessage
            else -> ResponseMessage(false,"")
        }
    }

    private fun saveInstance(logInModel: LogInModel, rememberMe: Boolean){
        if (rememberMe)
            UserPreference.saveString(UserPreference.SESSION, logInModel.cashierID!!)
        UserPreference.saveString(UserPreference.USER_ID, logInModel.cashierID!!)
//        UserPreference.saveString(UserPreference.TOKEN, logInModel.token!!)
    }
}