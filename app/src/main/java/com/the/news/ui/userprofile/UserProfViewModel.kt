package com.the.news.ui.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.the.news.data.dbfirebase.model.UsersModel
import com.the.news.data.repository.RepositoryPublication

class UserProfViewModel:ViewModel() {

    private val repository: RepositoryPublication
    private val _myPublications = MutableLiveData<List<UsersModel>>()
    val myPublications: LiveData<List<UsersModel>> = _myPublications

    init {
        repository = RepositoryPublication().getInctance()
        repository.userProfile(_myPublications)
    }



     fun sendFollow(uid:String){
        repository.sendFollow(uid)
    }

    fun delleteFollow(uid: String){
        repository.delleteFollow(uid)
    }

}