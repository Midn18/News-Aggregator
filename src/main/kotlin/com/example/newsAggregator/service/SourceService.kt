package com.example.newsAggregator.service

import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.entity.SourceDB
import com.example.newsAggregator.repository.SourceRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SourceService(
    private val restTemplate: RestTemplate,
    private val sourceRepository: SourceRepository,
    newsApiProperties: NewsApiProperties,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(NewsService::class.java)
    private val url: String =
        "${newsApiProperties.baseUrl}/sources?api_token=${newsApiProperties.key}"

    fun getAllSources(): List<SourceDB> {
        logger.info("Retrieving all sources from: $url")

        val response = restTemplate.getForObject(url, String::class.java) ?: return emptyList()
        logger.info("Response from API: $response")

        return try {
            val jsonNode = objectMapper.readTree(response)
            val sourcesArray = jsonNode["data"]

            val sourcesList: List<SourceDB> = objectMapper.readValue(
                sourcesArray.toString(),
                object : TypeReference<List<SourceDB>>() {}
            )
            val filteredSources = verifyExistingSourcesInDb(sourcesList)

            sourceRepository.saveAll(filteredSources)
        } catch (e: Exception) {
            logger.error("Error parsing sources response: ${e.message}", e)
            emptyList()
        }
    }

    private fun verifyExistingSourcesInDb(sourcesList: List<SourceDB>): List<SourceDB> {
        val existingSources = sourceRepository.findAll()
        val existingSourcesIds = existingSources.map { it.sourceId }

        return sourcesList.filter { !existingSourcesIds.contains(it.sourceId) }
    }
}