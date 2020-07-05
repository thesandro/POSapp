package com.possystem.posapp.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.models.ProductSell
import com.possystem.posapp.network.model.Product
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object DataLoader {
    //private const val BASE_PATH = "https://possystem.conveyor.cloud/Product/"
    private const val BASE_PATH = "https://ktorhighsteaks.herokuapp.com/"
    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    private val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private val retrofitClient: Retrofit = Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_PATH)
        .build()

    private val service: Service = retrofitClient.create(Service::class.java)

    suspend fun getProduct(parameters: Map<String, String>) = service.getProductResponse("GetByBarcode",parameters)
    suspend fun sellProducts(products: List<ProductSell>) = service.sellProducts("sell",products)

    interface Service {
        @FormUrlEncoded
        @POST("{path}")
        suspend fun getProductResponse(@Path("path") path: String, @FieldMap parameters: Map<String,String>): ProductEntry

        @POST("{path}")
        suspend fun sellProducts(@Path("path") path:String, @Body products: List<ProductSell>):String
    }
}
