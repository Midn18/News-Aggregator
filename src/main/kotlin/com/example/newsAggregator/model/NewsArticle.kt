package com.example.newsAggregator.model

import java.util.UUID

data class NewsArticle(
    val uuid: UUID,
    val title: String,
    val description: String,
    val keywords: String,
    val snippet: String,
    val url: String,
    val imageUrl: String,
    val language: String,
    val publishedAt: String,
    val source: String,
    val categories: List<String>
)