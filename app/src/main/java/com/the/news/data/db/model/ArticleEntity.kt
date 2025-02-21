package com.the.news.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "articles")
class ArticleEntity (

    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "ARTICLE_ID")
    val article_id: String,

    @ColumnInfo(name = "TITLE")
    val title: String,

    @ColumnInfo(name = "DESCRIPTION")
    val description: String,

    @ColumnInfo(name = "IMAGE_URL")
    val image_url: String,

    @ColumnInfo(name = "PUBDATE")
    val pubDate: String,

    @ColumnInfo(name = "SOURCE_NAME")
    val source_name: String,

    @ColumnInfo(name = "ISFAVOURITE")
    val isFavourite:Boolean = false

)