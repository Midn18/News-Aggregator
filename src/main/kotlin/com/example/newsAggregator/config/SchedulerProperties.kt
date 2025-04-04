package com.example.newsAggregator.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "scheduler.cron")
class SchedulerProperties {
    lateinit var newsFetcher: String
}