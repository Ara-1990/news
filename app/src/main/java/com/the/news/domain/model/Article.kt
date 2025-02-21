package com.the.news.domain.model

import android.os.Parcelable
import com.the.news.domain.Searchable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val article_id: String,
    override val title: String,
    val description: String,
    val image_url: String,
    val pubDate: String,
    val source_name: String,
    override var isFavourite: Boolean = false,
) : Searchable, Marcable, Parcelable
