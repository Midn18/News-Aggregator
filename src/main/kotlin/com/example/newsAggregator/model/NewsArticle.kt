package com.example.newsAggregator.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "news")
data class NewsArticle(
    @Id val uuid: String,
    val title: String = "",
    val description: String = "",
    val keywords: String = "",
    val snippet: String = "",
    val url: String = "",
    val imageUrl: String = "",
    val language: String = "",
    val publishedAt: String? = "",
    val source: String = "",
    val categories: List<String> = emptyList()
)