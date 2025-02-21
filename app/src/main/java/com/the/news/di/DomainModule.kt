package com.the.news.di

import com.the.news.domain.interactors.ArticlesInteractor
import org.koin.dsl.module

val domainModule = module {
    factory { ArticlesInteractor(repository = get()) }
}