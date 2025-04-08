package com.example.newsAggregator.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "scheduler.cron")
data class SchedulerProperties(
    val newsFetcher: String
)