package com.example.newsAggregator.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "news")
data class NewsArticleDB(
    @Id val uuid: String,
    val title: String = "",
    val description: String = "",
    val keywords: String = "",
    val snippet: String = "",
    val url: String = "",
    @Field("image_url") val imageUrl: String = "",
    val language: String = "",
    @Field("published_at") val publishedAt: String?,
    val source: String = "",
    val categories: List<String> = emptyList()
)
