package com.the.news.data.repository

import com.the.news.data.db.ArticleDao
import com.the.news.data.db.mapper.ArticleDbMapper
import com.the.news.data.network.NewsApi
import kotlin.coroutines.CoroutineContext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.the.news.data.network.*
import com.the.news.data.network.mappers.ArticleMapper

import com.the.news.domain.model.Article
import kotlinx.coroutines.withContext
import timber.log.Timber

class ArticleRepository(
    private val api: NewsApi,
private val dao: ArticleDao,
private val coroutineContext: CoroutineContext ){


    suspend fun getTopHeadlines(
        lang: Language?,
        apiKey: String
    ): Request<Pair<Int, List<Article>>> =
        try {
            val response = withContext(coroutineContext) {

                api.getTopHeadlines( lang?.title, apiKey)
            }
            getMappedResponse(response, ArticleMapper)
        } catch (ex: Exception) {
            Timber.d("getEmployeesSync request error: %s", ex.message)
            Request.Error.ConnectionIssue
        }

    suspend fun searchArticles(query: String?, lang: Language?, apiKey:String
    ): Request<Pair<Int, List<Article>>> =
        try {
            val response = withContext(coroutineContext) {
                api.searchArticles(query, lang?.title, apiKey
                )
            }
            getMappedResponse(response, ArticleMapper)
        } catch (ex: Exception) {
            Timber.d("getEmployeesSync request error: %s", ex.message)
            Request.Error.ConnectionIssue
        }

    suspend fun saveArticle(article: Article) {
        dao.saveArticle(ArticleDbMapper.toEntity(article))
    }


    fun getSavedArticles(): Flow<List<Article>> {
        return dao.getArticles().map { list ->
            list?.map { ArticleDbMapper.fromEntity(it) } ?: listOf()
        }
    }

    suspend fun removeArticleFromCache(article: Article) {
        dao.removeArticle(article.title)
    }

}