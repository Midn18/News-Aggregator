package com.example.newsAggregator.service

import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.model.Source
import com.example.newsAggregator.repository.SourceRepository
import com.example.newsAggregator.utils.GetSourceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SourceService(
    private val restTemplate: RestTemplate,
    newsApiProperties: NewsApiProperties,
    private val sourceRepository: SourceRepository
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
            val filteredSources = verifyExistingSourcesInDb(response.data)
            sourceRepository.saveAll(filteredSources)

            filteredSources
        } catch (e: Exception) {
            logger.error("Error parsing sources response: ${e.message}", e)
            emptyList()
        }
    }

    private fun verifyExistingSourcesInDb(sourcesList: List<Source>): List<Source> {
        val existingSources = sourceRepository.findAll()
        val existingSourcesIds = existingSources.map { it.sourceId }

        return sourcesList.filter { !existingSourcesIds.contains(it.sourceId) }
    }
}