package com.example.newsAggregator.service

import com.example.newsAggregator.config.NewsApiProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AllNewsService(
    private val restTemplate: RestTemplate,
    private val newsApiProperties: NewsApiProperties
) {
    private val logger: Logger = LoggerFactory.getLogger(AllNewsService::class.java)
    private val url: String =
        "${newsApiProperties.baseUrl}/headlines?locale=us&language=en&api_token=${newsApiProperties.key}"

    fun getAllNews(): String? {
        logger.info("Retrieving all news from: $url")

        return try {
            val response = restTemplate.getForObject(url, String::class.java) ?: "No data received"
            logger.info("Response from API: $response")
            response
        } catch (e: Exception) {
            logger.error("Error fetching news: ${e.message}", e)
            "Unexpected error occurred, please try again"
        }
    }
}