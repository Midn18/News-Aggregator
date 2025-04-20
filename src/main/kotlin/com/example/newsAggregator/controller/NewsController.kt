package com.example.newsAggregator.controller

import com.example.newsAggregator.model.Source
import com.example.newsAggregator.services.NewsService
import com.example.newsAggregator.services.SourceService
import com.example.newsAggregator.utils.NewsCategoryEnum
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api/news")
class NewsController(
    private val newsService: NewsService,
    private val sourceService: SourceService
) {

    @GetMapping("/article/page")
    fun getNewsArticleByParams(
        @RequestParam(name = "page_number", required = false)
        @Min(1, message = "Page number must be greater than 0")
        pageNumber: Int?,
        @RequestParam(name = "page_size", required = false)
        @Min(1, message = "Page size must be at least 1")
        @Max(100, message = "Page size cannot be more than 100")
        pageSize: Int?,
        @RequestParam(name = "category", required = false) category: String?,
        @RequestParam(name = "search", required = false) search: String?,
    ): ResponseEntity<out Any> {
        val categoryEnum = NewsCategoryEnum.from(category)
        if (category != null && categoryEnum == null) {
            throw IllegalArgumentException("Invalid category: $category")
        }

        return ResponseEntity.ok(
            newsService.getNewsByParams(
                pageNumber = pageNumber ?: 1,
                pageSize = pageSize ?: 10,
                categoryEnum?.value,
                search
            )
        )
    }

    @GetMapping("/source")
    fun getAllSources(): List<Source>? {
        return sourceService.getAllSources()
    }
}