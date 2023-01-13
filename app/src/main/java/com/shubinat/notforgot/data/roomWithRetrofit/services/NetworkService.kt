package com.shubinat.notforgot.data.roomWithRetrofit.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.shubinat.notforgot.data.roomWithRetrofit.apis.NotForgotApi
import com.shubinat.notforgot.data.roomWithRetrofit.converterfactories.NullOnEmptyConverterFactory
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService private constructor() {
    private val retrofit: Retrofit = createRetrofitInstance()

    private fun createRetrofitInstance(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    companion object {
        private var INSTANCE: NetworkService? = null
        private const val BASE_URL = "http://192.168.1.45/notforgot/api/"
        fun getInstance(): NetworkService {
            if (INSTANCE == null)
                INSTANCE = NetworkService()
            return INSTANCE as NetworkService
        }
    }

    fun getNotForgotApi(): NotForgotApi {
        return retrofit.create(NotForgotApi::class.java)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}