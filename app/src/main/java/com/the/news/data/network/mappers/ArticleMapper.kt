package com.the.news.data.network.mappers

import com.the.news.data.network.response.ArticleResponse
import com.the.news.data.network.response.NewsResponse
import com.the.news.domain.model.Article

object ArticleMapper : ResponseMapper<NewsResponse, Pair<Int, List<Article>>?> {

    private val mapper by lazy {
        object : Mapper<ArticleResponse, Article> {
            override fun transform(from: ArticleResponse): Article? {
                return if (from.title != null && from.description != null) {

                    Article(
                        article_id = from.article_id?: "",
                        title = from.title,
                        description = from.description ?: "",
                        image_url = from.image_url ?: "",
                        pubDate = from.pubDate ?: "",
                        source_name = from.source_name ?: "",
                        )
                } else {
                    null
                }
            }
        }
    }

    override fun modelFromResponse(response: NewsResponse): Pair<Int, List<Article>>? {
        return with(response) {
            if (cout != null && !results.isNullOrEmpty()) {
                cout to mapper.mapSecure(results)
            } else {
                null
            }
        }
    }
}