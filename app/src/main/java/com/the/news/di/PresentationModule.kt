package com.the.news.di

import com.the.news.ui.details.DetailsViewModel
import com.the.news.ui.favourite.FavouriteViewModel
import com.the.news.ui.news.NewsViewModel
import com.the.news.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { NewsViewModel(application = androidApplication(), interactor = get()) }
    viewModel { SearchViewModel(application = androidApplication(), interactor = get()) }
    viewModel { DetailsViewModel(application = androidApplication()) }
    viewModel { FavouriteViewModel(application = androidApplication(), interactor = get()) }
}