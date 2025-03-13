package com.the.news.ui.news

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.the.news.data.network.Request
import com.the.news.domain.interactors.ArticlesInteractor
import com.the.news.ui.BaseMviViewModel
import com.the.news.ui.ScreenStatus
import com.the.news.ui.news.Event.*

import com.the.news.ui.news.Effect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel (application: Application,
                     private val interactor: ArticlesInteractor
): BaseMviViewModel<State, Effect, Event>(application) {


    val API_KEY = ""

    private var state = State()

    init {
        fetchArticles()
        getSavedArticles()
    }

    override fun process(event: Event) {
        super.process(event)
        when (event) {
            is RemoveFromFavourites -> deleteArticleFromCache(event.title)
            is MarkAsFavourite -> saveArticle(event.title)
            is ViewDetails -> {
                state.articles.find { it.title == event.title }?.let {
                    viewEffect = NavigateTo.Details(it)
                }
            }

            else -> {}
        }
    }

    private fun saveArticle(title: String) {
        state.articles.find { it.title == title }?.let { article ->
            state.status = ScreenStatus.LOADING
            viewState = state
            viewModelScope.launch {
                interactor.saveArticle(article)
                withContext(Dispatchers.Main) {
                    state.status = ScreenStatus.FETCHED
                    viewState = state
                }
            }
        }
    }

    private fun deleteArticleFromCache(title: String) {
        state.articles.find { it.title == title }?.let { article ->
            state.status = ScreenStatus.LOADING
            viewState = state
            viewModelScope.launch {
                interactor.removeArticleFromCache(article)
                withContext(Dispatchers.Main) {
                    state.status = ScreenStatus.FETCHED
                    viewState = state
                }
            }
        }
    }

    private fun fetchArticles() {
        state.status = ScreenStatus.LOADING
        viewState = state
        viewModelScope.launch {
            val result = interactor.getTopHeadlines(null, API_KEY)

            withContext(Dispatchers.Main) {
                when (result) {
                    is Request.Success.Network -> {
                        state.articlesFromRemote = result.data.second
                    }
                    else -> {}
                }
                state.status = ScreenStatus.FETCHED
                viewState = state
            }
        }
    }

    private fun getSavedArticles() {
        state.status = ScreenStatus.LOADING
        viewState = state
        viewModelScope.launch {
            interactor.getSavedArticles().collect { result ->
                withContext(Dispatchers.Main) {
                    state.savedArticlesTitles = result.map { it.title }
                    state.status = ScreenStatus.FETCHED
                    viewState = state
                }
            }
        }
    }
}

