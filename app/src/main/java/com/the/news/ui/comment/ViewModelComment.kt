package com.the.news.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.the.news.data.dbfirebase.model.CommentModel
import com.the.news.data.repository.RepositoryPublication

class ViewModelComment : ViewModel(){
    private val repository: RepositoryPublication
    private val _allcomments = MutableLiveData<List<CommentModel>>()
    val allcomments: LiveData<List<CommentModel>> = _allcomments

    init { repository = RepositoryPublication().getInctance() }

    fun deleteComment(comment: String){
        repository.deleteComments(comment)
    }

    fun getComments( pubId:String){
        repository.getComments(_allcomments,  pubId)
    }

}