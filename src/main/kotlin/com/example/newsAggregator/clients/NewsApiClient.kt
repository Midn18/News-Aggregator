package com.example.newsAggregator.clients

import NewsArticleResponseBody
import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.config.SchedulerProperties
import com.example.newsAggregator.services.NewsService
import com.example.newsAggregator.utils.GetNewsArticlesResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Service
class NewsApiClient(
    newsApiProperties: NewsApiProperties,
    schedulerProperties: SchedulerProperties,
    private val restTemplate: RestTemplate
) {
    val url: String = "${newsApiProperties.baseUrl}/all?api_token=${newsApiProperties.key}"
    private val logger: Logger = LoggerFactory.getLogger(NewsService::class.java)

    fun getAllNewsByParams(
        pageSize: Int?,
        language: String?
    ): GetNewsArticlesResponse? {
        val uri = UriComponentsBuilder.fromHttpUrl(url)
            .queryParamIfPresent("limit", Optional.ofNullable(pageSize))
            .queryParamIfPresent("language", Optional.ofNullable(language))
            .toUriString()

        logger.info("Request URL: $uri")
        restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            GetNewsArticlesResponse::class.java
        ).body?.let { response ->
            return response
        } ?: return null
    }

    @Scheduled(cron = "#{schedulerProperties.newsFetcher}")
    fun fetchNewsPeriodically() {
        getAllNewsByParams(3, "en")?.let { response ->
            logger.info("Fetched news articles: $response")
        } ?: logger.warn("Failed to fetch news articles")
    }
}