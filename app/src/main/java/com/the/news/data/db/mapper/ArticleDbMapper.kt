package com.the.news.data.db.mapper

import com.the.news.data.db.DbMapper
import com.the.news.data.db.model.ArticleEntity
import com.the.news.domain.model.Article

object ArticleDbMapper: DbMapper<Article, ArticleEntity> {
    override fun toEntity(model: Article): ArticleEntity = with(model) {
        ArticleEntity( article_id, title,  description, image_url, pubDate, source_name,true

        )
    }


    override fun fromEntity(entity: ArticleEntity): Article = with(entity) {
        Article( article_id, title,  description, image_url, pubDate, source_name,  true)



    }
}
