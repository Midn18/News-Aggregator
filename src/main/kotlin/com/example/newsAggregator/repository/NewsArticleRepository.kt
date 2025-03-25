package com.example.newsAggregator.repository

import com.example.newsAggregator.entity.NewsArticleDB
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsArticleRepository: MongoRepository<NewsArticleDB, String>
