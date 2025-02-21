package com.the.news.data.db

interface DbMapper <MODEL, ENTITY> {

    fun toEntity(model: MODEL): ENTITY

    fun fromEntity(entity: ENTITY): MODEL
}