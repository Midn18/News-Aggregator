package com.example.newsAggregator.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "source")
data class SourceDB(
    @Id
    @JsonProperty("source_id") val sourceId: String,
    val domain: String,
    val language: String,
    val locale: String?,
    val categories: List<String>
)