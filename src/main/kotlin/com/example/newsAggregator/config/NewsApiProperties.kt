package com.example.newsAggregator.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "api.news-api")
data class NewsApiProperties(
     val baseUrl: String,
     val key: String
)