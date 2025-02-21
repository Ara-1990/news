package com.the.news.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.the.news.data.dbfirebase.model.UsersModel
import com.the.news.data.repository.RepositoryPublication


class ViewModelPublication : ViewModel() {

    private val repository: RepositoryPublication
    private val _allPublications = MutableLiveData<List<UsersModel>>()
    val allPublications:LiveData<List<UsersModel>> = _allPublications



    init {
        repository = RepositoryPublication().getInctance()
        repository.getPosts(_allPublications)

    }



}

