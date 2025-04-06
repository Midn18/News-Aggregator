package com.example.newsAggregator.config

import LoggingInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate(
            BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory())
        )
        restTemplate.interceptors.add(LoggingInterceptor())
        return restTemplate
    }
}