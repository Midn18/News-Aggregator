package com.example.newsAggregator.service

import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.model.NewsArticle
import com.example.newsAggregator.model.Source
import com.example.newsAggregator.repository.SourceRepository
import com.example.newsAggregator.utils.GetSourceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SourceService(
    private val restTemplate: RestTemplate,
    newsApiProperties: NewsApiProperties,
    private val sourceRepository: SourceRepository,
    private val mongoTemplate: MongoTemplate
) {
    private val logger: Logger = LoggerFactory.getLogger(NewsService::class.java)
    private val url: String =
        "${newsApiProperties.baseUrl}/sources?api_token=${newsApiProperties.key}"

    fun getAllSources(): List<Source> {
        logger.info("Retrieving all sources from: $url")

        val response: GetSourceResponse =
            restTemplate.getForObject(url, GetSourceResponse::class.java) ?: return emptyList()
        logger.info("Response from API: $response")

        return try {
            val filteredSources = filterNewSources(response.data)
            sourceRepository.saveAll(filteredSources)

            filteredSources
        } catch (e: Exception) {
            logger.error("Error parsing sources response: ${e.message}", e)
            emptyList()
        }
    }

    private fun filterNewSources(sourcesList: List<Source>): List<Source> {
        val newIds = sourcesList.map { it.sourceId }
        val query = Query(Criteria.where("_id").`in`(newIds))
        val existingSources: Set<String> = mongoTemplate.find(query, Source::class.java)
            .map { it.sourceId }
            .toSet()

        return sourcesList.filter { it.sourceId !in existingSources }
    }
}