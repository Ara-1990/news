package com.the.news.data.network.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(
   @SerializedName("results") val results: List<ArticleResponse>?,
    @SerializedName("totalResults") val cout: Int?
)
