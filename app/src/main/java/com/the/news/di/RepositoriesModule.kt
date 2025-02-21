package com.the.news.di


import com.the.news.data.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoriesModule = module {
    single {
        ArticleRepository(
            api = get(),
            dao = get(),
            coroutineContext = Dispatchers.IO,
        )
    }
}