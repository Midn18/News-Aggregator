package com.example.newsAggregator.services

import com.example.newsAggregator.clients.NewsApiClient
import com.example.newsAggregator.model.NewsArticle
import com.example.newsAggregator.repository.NewsArticleRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class NewsService(
    private val newsApiClient: NewsApiClient,
    private val newsArticleRepository: NewsArticleRepository,
    private val mongoTemplate: MongoTemplate
) {
    private val logger: Logger = LoggerFactory.getLogger(NewsService::class.java)

    @Scheduled(cron = "\${scheduler.cron.news-fetcher}")
    fun getNewsArticles() {
        try {
            val response = newsApiClient.getNewsArticles(3, "ro")
            val newsList = response?.data ?: emptyList()

            val filtered = filterNewNewsArticles(newsList)

            if (filtered.isNotEmpty()) {
                newsArticleRepository.saveAll(filtered)
                logger.info("Saved ${filtered.size} new articles.")
            } else {
                logger.info("No new articles to save.")
            }
        } catch (e: Exception) {
            logger.error("Error fetching/saving news: ${e.message}", e)
        }
    }

    fun getNewsByParams(pageNumber: Int, pageSize: Int, category: String?, search: String?): List<NewsArticle> {
        if (pageNumber !in 1..10)
            throw IllegalArgumentException("Page number must be between 1 and 10")

        val pageRequest = PageRequest.of(pageNumber - 1, pageSize)
        val news = newsArticleRepository.findAllNewsByParams(
            pageRequest,
            category ?: "",
            search ?: ""
        )
        return news.content
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