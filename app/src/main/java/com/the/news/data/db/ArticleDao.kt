package com.the.news.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.the.news.data.db.model.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles WHERE TITLE == :title")
    suspend fun getArticleByTitle(title: String): ArticleEntity?

    @Query("SELECT * FROM articles")
    fun getArticles(): Flow<List<ArticleEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(articleEntity: ArticleEntity)

    @Query("DELETE FROM articles WHERE TITLE == :title")
    suspend fun removeArticle(title: String)
}