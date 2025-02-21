package com.the.news.ui.search


import android.app.Application

import androidx.lifecycle.viewModelScope
import com.the.news.data.network.Request
import com.the.news.domain.interactors.ArticlesInteractor
import com.the.news.ui.BaseMviViewModel
import com.the.news.ui.ScreenStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel (application: Application, private val interactor: ArticlesInteractor)
    : BaseMviViewModel<State, Effect, Event,>(application) {

    val API_KEY = "pub_5697910feb7a534c609980f9199058ba59ed9"

    private var state = State()

    override fun process(event: Event) {
        super.process(event)
        when (event) {
            is Event.Search -> {
                state.searchQuery = event.data
                searchArticleByQuery()
            }
        }
    }

    private fun searchArticleByQuery() {
        if (state.searchQuery.isNotEmpty()) {
            state.status = ScreenStatus.LOADING
            viewState = state
            viewModelScope.launch {
                val result = interactor.search(state.searchQuery, null, API_KEY
                )
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Request.Success.Network -> {
                            state.articles = result.data.second
                        }
                        else -> {}
                    }
                    state.status = ScreenStatus.FETCHED
                    viewState = state
                }
            }
        } else {
            state.articles = listOf()
            viewState = state
        }
    }

}
