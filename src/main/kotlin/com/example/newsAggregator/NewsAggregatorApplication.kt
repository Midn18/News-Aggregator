package com.example.newsAggregator

import com.example.newsAggregator.config.NewsApiProperties
import com.example.newsAggregator.config.SchedulerProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(NewsApiProperties::class, SchedulerProperties::class)
class NewsAggregatorApplication

fun main(args: Array<String>) {
    runApplication<NewsAggregatorApplication>(*args)
}
