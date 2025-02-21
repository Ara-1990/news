package com.the.news.ui

interface ViewModelContract <EVENT> {
    fun process(event: EVENT)

}