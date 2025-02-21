package com.the.news.ui.news

import com.the.news.domain.model.Article
import com.the.news.ui.BaseEffect
import com.the.news.ui.BaseEvent
import com.the.news.ui.BaseState

data class State(
    var articlesFromRemote: List<Article> = listOf(),
    var savedArticlesTitles: List<String> = listOf()
) : BaseState() {

    val articles: List<Article>
        get() = articlesFromRemote.onEach { item ->
            item.isFavourite = savedArticlesTitles.contains(item.title)
        }
}

sealed class Effect : BaseEffect() {
    sealed class NavigateTo : Effect() {
        data class Details(val article: Article) : NavigateTo()
    }

}

sealed class Event : BaseEvent() {
    data class ViewDetails(val title: String) : Event()
    data class MarkAsFavourite(val title: String) : Event()
    data class RemoveFromFavourites(val title: String) : Event()
}