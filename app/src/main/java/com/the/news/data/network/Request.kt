package com.the.news.data.network

sealed class Request<out T> {

    sealed class Success<T> : Request<T>() {
        data class Network<T>(val data: T) : Success<T>()

    }

    sealed class Error : Request<Nothing>() {
        object EmptyResponse : Error()
        object ResponseNotSucceed : Error()
        object ConnectionIssue : Error()
        data class Other(val message: String, val code: Int? = null) : Error()
    }
}

