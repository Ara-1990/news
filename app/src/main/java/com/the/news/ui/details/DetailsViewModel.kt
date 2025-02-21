package com.the.news.ui.details

import android.app.Application
import com.the.news.ui.BaseMviViewModel

class DetailsViewModel (application: Application) :
    BaseMviViewModel<State, Effect, Event>(application) {

    private var state = State()

    override fun process(event: Event) {
        super.process(event)
        when (event) {
            is Event.InitScreen -> {
                state.article = event.article
                viewState = state
            }
        }
    }
}
