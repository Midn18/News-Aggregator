package com.example.newsAggregator.controller

import com.example.newsAggregator.service.AllNewsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/news")
class HeadlineNewsController(private val allNewsService: AllNewsService) {

    @GetMapping("/")
    fun getAllNews(): String? {
        return allNewsService.getAllNews()
    }
}