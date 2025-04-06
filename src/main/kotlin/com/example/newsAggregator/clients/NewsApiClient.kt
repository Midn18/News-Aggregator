package com.example.newsAggregator.clients

import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.services.NewsService
import com.example.newsAggregator.utils.GetNewsArticlesResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Service
class NewsApiClient(
    newsApiProperties: NewsApiProperties,
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

        if (logger.isInfoEnabled) {
            logger.info("Request URL: $uri")
        }

        return restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            GetNewsArticlesResponse::class.java
        ).body
    }

    fun getNewsArticleScheduledJob(pageSize: Int?, language: String?): GetNewsArticlesResponse? {
        val uri = UriComponentsBuilder.fromHttpUrl(url)
            .queryParamIfPresent("limit", Optional.ofNullable(pageSize))
            .queryParamIfPresent("language", Optional.ofNullable(language))
            .toUriString()

        logger.info("Request URL: $uri")
        return restTemplate.exchange(uri, HttpMethod.GET, null, GetNewsArticlesResponse::class.java).body
    }
}