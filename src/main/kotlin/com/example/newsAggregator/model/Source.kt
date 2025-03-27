package com.example.newsAggregator.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "source")
data class Source(
    @Id
    @JsonProperty("source_id") val sourceId: String,
    val domain: String,
    val language: String,
    val locale: String?,
    val categories: List<String>
)