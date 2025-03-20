package com.example.news_aggregator_project.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "api.news-service")
class NewsApiProperties {
    lateinit var baseUrl: String
    lateinit var key: String
}