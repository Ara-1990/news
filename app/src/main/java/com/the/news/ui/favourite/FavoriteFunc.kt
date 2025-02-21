package com.the.news.ui.favourite


import com.the.news.domain.model.Article
import com.the.news.ui.BaseEffect
import com.the.news.ui.BaseEvent
import com.the.news.ui.BaseState


data class State(
    var savedArticled: List<Article> = listOf()
) : BaseState()

sealed class Effect : BaseEffect() {
    sealed class NavigateTo : Effect() {
        data class Details(val article: Article) : NavigateTo()
    }

}

sealed class Event : BaseEvent() {
    data class ViewDetails(val title: String) : Event()
    data class Remove(val title: String) : Event()
}