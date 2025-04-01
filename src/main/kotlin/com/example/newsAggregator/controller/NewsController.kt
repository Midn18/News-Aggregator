package com.example.newsAggregator.controller

import NewsArticleResponseBody
import com.example.newsAggregator.model.Source
import com.example.newsAggregator.service.NewsService
import com.example.newsAggregator.service.SourceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/news")
class NewsController(
    private val newsService: NewsService,
    private val sourceService: SourceService
) {

    @GetMapping("/article")
    fun getNewsArticle(
        @RequestParam(name = "page_size", defaultValue = "3", required = false) limit: Int?,
        @RequestParam(name = "language", defaultValue = "en", required = false) language: String?
    ): NewsArticleResponseBody? {
        return newsService.getAllNewsByParams(limit, language)
    }

    @GetMapping("/source")
    fun getAllSources(): List<Source>? {
        return sourceService.getAllSources()
    }
}