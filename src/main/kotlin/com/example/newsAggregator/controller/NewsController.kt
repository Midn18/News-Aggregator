package com.example.newsAggregator.controller

import NewsArticleResponseBody
import com.example.newsAggregator.entity.NewsArticleDB
import com.example.newsAggregator.entity.SourceDB
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

    @GetMapping("/all")
    fun getAllNews(): List<NewsArticleDB>? {
        return newsService.getAllNews()
    }

    @GetMapping("/allNewsByParams")
    fun getNewsByParams(
        @RequestParam(name = "page_size") limit: Int
    ): NewsArticleResponseBody? {
        return newsService.getAllNewsByParams(limit)
    }

    @GetMapping("/allNewsFromDb")
    fun getAllNewsFromDb(): List<NewsArticleDB>? {
        return newsService.getAllNewsFromDb()
    }

    @GetMapping("/sources")
    fun getAllSources(): List<SourceDB>? {
        return sourceService.getAllSources()
    }
}