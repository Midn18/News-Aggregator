package com.example.newsAggregator.service

import NewsArticleResponseBody
import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.entity.NewsArticleDB
import com.example.newsAggregator.repository.NewsArticleRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class NewsService(
    private val restTemplate: RestTemplate,
    newsApiProperties: NewsApiProperties,
    private val newsArticleRepository: NewsArticleRepository,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(NewsService::class.java)
    private val url: String =
        "${newsApiProperties.baseUrl}/all?api_token=${newsApiProperties.key}"

    fun getAllNews(): List<NewsArticleDB> {
        logger.info("Retrieving all news from: $url")

        val response = restTemplate.getForObject(url, String::class.java) ?: return emptyList()
        logger.info("Response from API: $response")

        return try {
            val jsonNode = objectMapper.readTree(response)
            val newsArray = jsonNode["data"]

            val newsList: List<NewsArticleDB> = objectMapper.readValue(
                newsArray.toString(),
                object : TypeReference<List<NewsArticleDB>>() {}
            )
            val filteredNews = verifyExistingNewsInDb(newsList)

            newsArticleRepository.saveAll(filteredNews)
        } catch (e: Exception) {
            logger.error("Error parsing news response: ${e.message}", e)
            emptyList()
        }
    }

    fun getAllNewsByParams(pageSize: Int): NewsArticleResponseBody {
        val urlWithParams = "$url&limit=$pageSize"
        logger.info("Retrieving news from: $urlWithParams")

        val response = restTemplate.getForObject(urlWithParams, String::class.java) ?: return NewsArticleResponseBody(
            emptyList(),
            pageSize
        )
        logger.info("Response from API: $response")

        return try {
            val jsonNode = objectMapper.readTree(response)
            val newsArray = jsonNode["data"]

            val newsList: List<NewsArticleDB> = objectMapper.readValue(
                newsArray.toString(),
                object : TypeReference<List<NewsArticleDB>>() {}
            )
            val filteredNews = verifyExistingNewsInDb(newsList)

            newsArticleRepository.saveAll(filteredNews)

            NewsArticleResponseBody(filteredNews, pageSize)
        } catch (e: Exception) {
            logger.error("Error parsing news response: ${e.message}", e)
            NewsArticleResponseBody(emptyList(), pageSize)
        }
    }

    fun getAllNewsFromDb(): List<NewsArticleDB> {
        return newsArticleRepository.findAll()
    }

    private fun verifyExistingNewsInDb(newsList: List<NewsArticleDB>): List<NewsArticleDB> {
        val existingNews = newsArticleRepository.findAll()
        val existingNewsUuids = existingNews.map { it.uuid }
        return newsList.filter { it.uuid !in existingNewsUuids }
    }
}