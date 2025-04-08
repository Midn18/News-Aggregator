package com.example.newsAggregator.controller

import com.example.newsAggregator.model.NewsArticle
import com.example.newsAggregator.model.Source
import com.example.newsAggregator.services.NewsService
import com.example.newsAggregator.services.SourceService
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

    @GetMapping("/article/page")
    fun getNewsArticleByPageNumber(
        @RequestParam(name = "page_number", defaultValue = "1", required = true) pageNumber: Int,
    ): List<NewsArticle> {
        return pageNumber.let { newsService.getNewsByPageNumber(it) }
    }

    @GetMapping("/source")
    fun getAllSources(): List<Source>? {
        return sourceService.getAllSources()
    }
}