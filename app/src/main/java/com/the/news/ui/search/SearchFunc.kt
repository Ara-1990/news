package com.the.news.ui.search



import com.the.news.domain.model.Article
import com.the.news.ui.BaseEffect
import com.the.news.ui.BaseEvent
import com.the.news.ui.BaseState


data class State (
    var articles: List<Article> = listOf(),
    var searchQuery: String = ""
) : BaseState()

sealed class Effect : BaseEffect() {}

sealed class Event : BaseEvent() {
    data class Search(val data: String) : Event()
}