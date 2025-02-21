package com.the.news.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.the.news.data.dbfirebase.model.UsersModel
import com.the.news.data.repository.RepositoryPublication

class ViewModelFollow : ViewModel(){
    private val repository: RepositoryPublication
    private val _allPublications = MutableLiveData<List<UsersModel>>()
    val allPublications: LiveData<List<UsersModel>> = _allPublications

    init {
        repository = RepositoryPublication().getInctance()
        repository.getFollowed(_allPublications)
    }

    fun deletePost(curent_publication: String){
        repository.delletePost(curent_publication)
    }


}