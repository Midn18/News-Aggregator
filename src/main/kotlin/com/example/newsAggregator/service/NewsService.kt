package com.example.newsAggregator.service

import NewsArticleResponseBody
import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.model.NewsArticle
import com.example.newsAggregator.repository.NewsArticleRepository
import com.example.newsAggregator.utils.GetNewsArticlesResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class NewsService(
    private val restTemplate: RestTemplate,
    newsApiProperties: NewsApiProperties,
    private val newsArticleRepository: NewsArticleRepository,
    private val mongoTemplate: MongoTemplate
) {
    private val logger: Logger = LoggerFactory.getLogger(NewsService::class.java)
    private val url: String =
        "${newsApiProperties.baseUrl}/all?api_token=${newsApiProperties.key}"

    fun getAllNewsByParams(pageSize: Int?, language: String?): NewsArticleResponseBody {
        val urlWithParams = "$url&limit=$pageSize&language=$language"
        logger.info("Retrieving news from: $urlWithParams")

        val response: GetNewsArticlesResponse =
            restTemplate.getForObject(urlWithParams, GetNewsArticlesResponse::class.java)
                ?: return NewsArticleResponseBody(
                    emptyList(),
                    pageSize
                )
        logger.info("Response from API: $response")

        return try {
            val filteredNews = filterNewNewsArticles(response.data)

            newsArticleRepository.saveAll(filteredNews)

            NewsArticleResponseBody(filteredNews, pageSize)
        } catch (e: Exception) {
            logger.error("Error parsing news response: ${e.message}", e)
            NewsArticleResponseBody(emptyList(), pageSize)
        }
    }

    private fun filterNewNewsArticles(newsList: List<NewsArticle>): List<NewsArticle> {
        val newIds = newsList.map { it.uuid }

        val query = Query(Criteria.where("_id").`in`(newIds))
        val existingNewsUuids: Set<String> = mongoTemplate.find(query, NewsArticle::class.java)
            .map { it.uuid }
            .toSet()

        return newsList.filter { it.uuid !in existingNewsUuids }
    }
}