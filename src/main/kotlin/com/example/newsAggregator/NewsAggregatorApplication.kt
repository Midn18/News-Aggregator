package com.example.newsAggregator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class NewsAggregatorApplication

fun main(args: Array<String>) {
    runApplication<NewsAggregatorApplication>(*args)
}
