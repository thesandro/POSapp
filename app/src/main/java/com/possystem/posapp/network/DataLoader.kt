package com.possystem.posapp.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.network.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

object DataLoader {

    private const val BASE_PATH = "https://ktorhighsteaks.herokuapp.com/"
    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofitClient: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_PATH)
        .build()

    private val service: Service = retrofitClient.create(Service::class.java)

    suspend fun getProduct(parameters: Map<String, String>): ProductEntry {
        return service.getProductResponse("products",parameters)
    }

    interface Service {
        @FormUrlEncoded
        @POST("{path}")
        suspend fun getProductResponse(@Path("path") path: String, @FieldMap parameters: Map<String,String>): ProductEntry
    }
}
