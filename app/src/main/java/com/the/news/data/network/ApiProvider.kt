package com.the.news.data.network

import com.google.gson.Gson
import com.the.news.data.network.NewsApi.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiProvider (private val interceptors: List<Interceptor> = emptyList(),
                   private val gson: Gson) {



    private lateinit var api: NewsApi


    companion object {


        private const val CALL_TIMEOUT = 60L
        private const val CONNECTION_TIMEOUT = 30L
        private const val READ_TIMEOUT = 30L
        private const val WRITE_TIMEOUT = 30L


    }

    fun getMyServiceApi(): NewsApi = api

    fun init() {
        initMyServiceApi()
    }


    private fun initMyServiceApi() {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                OkHttpClient.Builder()
                    .apply {
                        connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
                        interceptors.forEach { addInterceptor(it) }
                    }
                    .build()
            )
            .build()
            .create(NewsApi::class.java)
    }


}