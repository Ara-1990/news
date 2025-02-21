package com.the.news.domain.interactors


import com.the.news.data.network.Language
import com.the.news.data.network.Request
import com.the.news.data.repository.ArticleRepository
import com.the.news.domain.model.Article
import kotlinx.coroutines.flow.Flow


class ArticlesInteractor (private val repository: ArticleRepository, ) {


    suspend fun getTopHeadlines(lang: Language?, apiKey: String):
            Request<Pair<Int, List<Article>>> = repository.getTopHeadlines( lang, apiKey)

    suspend fun search(query: String?, lang: Language?, apiKey:String
    ): Request<Pair<Int, List<Article>>> =
        repository.searchArticles(query, lang, apiKey)

    suspend fun saveArticle(article: Article) = repository.saveArticle(article)

    fun getSavedArticles(): Flow<List<Article>> = repository.getSavedArticles()

    suspend fun removeArticleFromCache(article: Article) {
        repository.removeArticleFromCache(article)
    }

}