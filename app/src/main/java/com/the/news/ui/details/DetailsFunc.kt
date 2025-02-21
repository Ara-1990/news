package com.the.news.ui.details




import com.the.news.domain.model.Article
import com.the.news.ui.BaseEffect
import com.the.news.ui.BaseEvent
import com.the.news.ui.BaseState


data class State(var article: Article? = null) : BaseState()


sealed class Effect : BaseEffect() {}

sealed class Event : BaseEvent() {
    data class InitScreen(val article: Article) : Event()
}