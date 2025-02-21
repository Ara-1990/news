package com.the.news.di


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.the.news.data.network.ApiProvider
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val networkModule = module {

    single<Gson> { GsonBuilder().create() }
    single {
        HttpLoggingInterceptor().apply {
            level = (HttpLoggingInterceptor.Level.BODY)
        }
    }

    single {
        ApiProvider(
            gson = get(),
            interceptors = listOf(
                get<HttpLoggingInterceptor>(),

            )
        )
    }
   single { get<ApiProvider>().getMyServiceApi() }
}