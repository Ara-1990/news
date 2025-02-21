package com.the.news.data.network

import com.the.news.data.network.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    companion object {
        val BASE_URL: String = "https://newsdata.io/api/1/"
    }

    @GET("latest?")
    suspend fun searchArticles(
        @Query("q") q: String?,
        @Query("language") language: String?,
        @Query("apikey") apiKey: String,

        ): Response<NewsResponse>


    @GET("latest?")
    suspend fun getTopHeadlines(

        @Query("language") language: String?,

        @Query("apikey") apiKey: String,

        ): Response<NewsResponse>
}