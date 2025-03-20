package com.example.news_aggregator_project.service

import com.example.news_aggregator_project.config.NewsApiProperties
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

    fun getAllNews(): String? {
        val url = "${newsApiProperties.baseUrl}/all/?api_token=${newsApiProperties.key}&limit=10"
        logger.info("Retrieving all news from: $url")

        return try {
            val response = restTemplate.getForObject(url, String::class.java) ?: "No data received"
            logger.info("Response from API: $response")
            response
        } catch (e: Exception) {
            logger.error("Error fetching news: ${e.message}", e)
            "Error fetching news: ${e.message}"
        }
    }
}