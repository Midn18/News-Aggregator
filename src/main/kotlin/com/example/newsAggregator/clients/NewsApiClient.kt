package com.example.newsAggregator.clients

import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.utils.GetNewsArticlesResponse
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

    fun getNewsArticles(
        pageSize: Int?,
        language: String?
    ): GetNewsArticlesResponse? {
        val uri = UriComponentsBuilder.fromHttpUrl(url)
            .queryParamIfPresent("limit", Optional.ofNullable(pageSize))
            .queryParamIfPresent("language", Optional.ofNullable(language))
            .toUriString()

        return restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            GetNewsArticlesResponse::class.java
        ).body
    }
}