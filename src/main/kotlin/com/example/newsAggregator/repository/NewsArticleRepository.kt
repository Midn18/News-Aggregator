package com.example.newsAggregator.repository

import com.example.newsAggregator.model.NewsArticle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

interface NewsArticleRepositoryExt {
    fun findAllNewsByParams(pageable: Pageable, category: String, search: String): Page<NewsArticle>
}

interface NewsArticleRepository : MongoRepository<NewsArticle, String>, NewsArticleRepositoryExt

@Repository
class NewsArticleRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : NewsArticleRepositoryExt {

    override fun findAllNewsByParams(pageable: Pageable, category: String, search: String): Page<NewsArticle> {
        val query = Query().with(pageable)
        if (category.isNotEmpty()) {
            query.addCriteria(Criteria.where("categories").`is`(category))
        }
        if (search.isNotEmpty()) {
            query.addCriteria(Criteria.where("title").regex(search, "i"))
        }
        val results = mongoTemplate.find(query, NewsArticle::class.java)
        return PageableExecutionUtils.getPage(results, pageable) {
            mongoTemplate.count(Query(), NewsArticle::class.java)
        }
    }
}