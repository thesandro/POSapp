package com.possystem.posapp.network

import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.possystem.posapp.App
import com.possystem.posapp.R
import com.possystem.posapp.data.db.ProductEntry
import com.possystem.posapp.models.ProductSell
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

object DataLoader {
    const val HTTP_200_OK = 200
    const val HTTP_201_CREATED = 201
    private const val HTTP_400_BAD_REQUEST = 400
    private const val HTTP_401_UNAUTHORIZED = 401
    private const val HTTP_404_NOT_FOUND = 404
    private const val HTTP_500_INTERNAL_SERVER_ERROR = 500
    private const val HTTP_204_NO_CONTENT = 204
    private const val HTTP_503_SERVICE_UNAVAILABLE = 503


    private const val BASE_PATH = "https://possystem.conveyor.cloud/Product/"
    //private const val BASE_PATH = "https://ktorhighsteaks.herokuapp.com/"
    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    private val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    //    private val httpClient = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
//        .readTimeout(30, TimeUnit.SECONDS)
//        .writeTimeout(15, TimeUnit.SECONDS)
//        .addInterceptor { chain ->
//            var token = App.instance.getAppToken()
//            if (UserSharedPreference.instance.getString(UserSharedPreference.USER_KEY).isNotEmpty())
//                token = "Bearer " + UserSharedPreference.instance.getString(UserSharedPreference.USER_KEY)
//            Log.d("Token", " $token")
//            val request = chain.request().newBuilder().addHeader("Authorization", token).addHeader("Content-Type", "application/json").build()
//            chain.proceed(request)
//        }
    private val retrofitClient: Retrofit = Retrofit.Builder()
        .client(
            OkHttpClient.Builder().addInterceptor(interceptor)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                 .build()
        )
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_PATH)
        .build()

    private val service: Service = retrofitClient.create(Service::class.java)

    suspend fun getProduct(parameters: Map<String, String>) =
        service.getProductResponse(Endpoints.GET_PRODUCT, parameters)

    suspend fun sellProducts(employeeId: String, products: List<ProductSell>): Response<String> {
        val jsonObject = JsonObject()
        val jsonArray = gson.toJsonTree(products)
        jsonObject.addProperty("cashierPin", employeeId)
        jsonObject.add("products", jsonArray)

        return service.sellProducts(
            Endpoints.SELL,
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
}

    suspend fun postForm(path:String,parameters: Map<String, String>):Response<String>{
        return service.postForm(path,parameters)
    }

    fun handleApiError(responseCode:Int,response: ResponseBody?):String{
        return when (responseCode) {
            HTTP_400_BAD_REQUEST -> handleErrorBody(response!!.string())
            HTTP_401_UNAUTHORIZED -> handleErrorBody(response!!.string())
            HTTP_404_NOT_FOUND -> handleErrorBody(response!!.string())
            HTTP_500_INTERNAL_SERVER_ERROR -> handleErrorBody(response!!.string())
            HTTP_204_NO_CONTENT -> handleErrorBody("")
            HTTP_503_SERVICE_UNAVAILABLE -> App.instance.getContext().resources.getString(R.string.service_unavailable)
            else -> App.instance.getContext().resources.getString(R.string.incorrect_request) + " " + App.instance.getContext().getString(R.string.an_error_occurred_please_try_again)
        }
    }
    private fun handleErrorBody(errorBody: String):String {
        try {
            val errorJson = JSONObject(errorBody)
            if (errorJson.has("message")) {
                    return App.instance.getContext().resources.getString(R.string.incorrect_request) + " " + errorJson.getString("message")
            }
            return App.instance.getContext().resources.getString(R.string.unknown_error)
        } catch (e: JSONException) {
            return App.instance.getContext().resources.getString(R.string.unknown_error)
        }
    }

    interface Service {
        @FormUrlEncoded
        @POST("{path}")
        suspend fun getProductResponse(
            @Path("path") path: String,
            @FieldMap parameters: Map<String, String>
        ): Response<ProductEntry>

        @POST("{path}")
        suspend fun sellProducts(
            @Path("path") path: String,
            @Body products: RequestBody
        ): Response<String>

        @FormUrlEncoded
        @POST("{path}")
        suspend fun postForm(
            @Path("path") path: String?,
            @FieldMap parameters: Map<String, String>
        ): Response<String>

    }
}
