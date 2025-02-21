package com.the.news.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.the.news.data.dbfirebase.model.UsersModel
import com.the.news.data.repository.RepositoryPublication

class ViewModelProfile:ViewModel() {
    private val repository: RepositoryPublication
    private val _myPublications = MutableLiveData<List<UsersModel>>()
    val myPublications: LiveData<List<UsersModel>> = _myPublications

    init {
        repository = RepositoryPublication().getInctance()
        repository.getMyPublications(_myPublications)
    }
    fun delletePost(curent_publication: String){
        repository.delletePost(curent_publication)
    }

}