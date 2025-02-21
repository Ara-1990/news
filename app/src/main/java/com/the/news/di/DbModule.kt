package com.the.news.di
import com.the.news.data.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    single { AppDatabase.create(androidApplication()) }
    single { get<AppDatabase>().dao }
}