package com.the.news.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.the.news.data.dbfirebase.model.UsersModel
import timber.log.Timber

open class  BaseMviViewModel <STATE : BaseState, EFFECT : BaseEffect, EVENT : BaseEvent>(application: Application) :
    AndroidViewModel(application), ViewModelContract<EVENT> {

    private var viewEvent: EVENT? = null
    private var viewEventComment: EVENT? = null

    private val _viewStates: MutableLiveData<STATE?> = MutableLiveData()
    fun viewStates(): LiveData<STATE?> = _viewStates

    protected var viewState: STATE? = null
        set(value) {
            Timber.d("viewState : $value")
            field = value
            _viewStates.value = value
        }

    private val _viewEffects: SingleLiveEvent<EFFECT?> = SingleLiveEvent()
    fun viewEffects(): SingleLiveEvent<EFFECT?> = _viewEffects

    protected var viewEffect: EFFECT? = null
        set(value) {
            Timber.d("viewEffect : $value")
            field = value
            _viewEffects.value = value
        }





    private val _viewComments: MutableLiveData<List<UsersModel>?> = MutableLiveData()
    fun viewComments(): LiveData<List<UsersModel>?> = _viewComments

    protected var viewComment: List<UsersModel>? = null
        set(value) {
            Timber.d("viewComment : $value")
            field = value
            _viewComments.value = value
        }




    private val _viewCommentsEfect: SingleLiveEvent<EFFECT?> = SingleLiveEvent()
    fun viewCommentsEffect(): SingleLiveEvent<EFFECT?> = _viewCommentsEfect

    protected var viewCommentEfect: EFFECT? = null
        set(value) {
            Timber.d("viewComment : $value")
            field = value
            _viewCommentsEfect.value = value
        }


    override fun process(event: EVENT) {
        Timber.d("viewEvent: $event")
        viewEvent = event
    }



    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }
}