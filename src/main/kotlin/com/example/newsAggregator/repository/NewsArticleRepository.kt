package com.example.newsAggregator.repository

import com.example.newsAggregator.model.NewsArticle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

interface NewsArticleRepositoryExt {
    fun findAllByPageNumber(pageable: Pageable): Page<NewsArticle>
}

interface NewsArticleRepository : MongoRepository<NewsArticle, String>, NewsArticleRepositoryExt

@Repository
class NewsArticleRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : NewsArticleRepositoryExt {

    override fun findAllByPageNumber(pageable: Pageable): Page<NewsArticle> {
        val results = mongoTemplate.find(Query().with(pageable), NewsArticle::class.java)
        return PageableExecutionUtils.getPage(results, pageable) {
            mongoTemplate.count(Query(), NewsArticle::class.java)
        }
    }
}