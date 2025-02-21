package com.the.news.data.network.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("article_id") val article_id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("pubDate") val pubDate: String?,
    @SerializedName("image_url") val image_url: String?,
    @SerializedName("source_name") val source_name: String?,
    )
