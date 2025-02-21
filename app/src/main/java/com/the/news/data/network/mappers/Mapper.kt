package com.the.news.data.network.mappers

interface Mapper <FROM, INTO> {

    fun mapSecure(from: List<FROM?>?): List<INTO> {
        val result: ArrayList<INTO> = arrayListOf()
        if (!from.isNullOrEmpty()) {
            from.forEach { item ->
                if (item != null) {
                    transform(item)?.let {
                        result.add(it)
                    }
                }
            }
        }
        return result
    }

    fun transform(from: FROM): INTO?
}