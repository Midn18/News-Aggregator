package com.example.newsAggregator.repositoryTests

import com.example.newsAggregator.model.NewsArticle
import com.example.newsAggregator.repository.NewsArticleRepository
import com.example.newsAggregator.repository.NewsArticleRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

@DataMongoTest
@Import(NewsArticleRepositoryImpl::class)
class NewsArticleRepositoryIntegrationTest {

    @Autowired
    private lateinit var newsArticleRepository: NewsArticleRepository

    @BeforeEach
    fun setup() {
        val alreadyPresent = newsArticleRepository.findById("test-1")
        if (alreadyPresent.isEmpty) {
            val articles = listOf(
                NewsArticle(
                    uuid = "test-1",
                    title = "Breaking Tech Innovation",
                    categories = listOf("tech"),
                    publishedAt = LocalDateTime.now().toString()
                ),
                NewsArticle(
                    uuid = "test-2",
                    title = "Latest in Business",
                    categories = listOf("business"),
                    publishedAt = LocalDateTime.now().toString()
                ),
                NewsArticle(
                    uuid = "test-3",
                    title = "General updates from today",
                    categories = listOf("general"),
                    publishedAt = LocalDateTime.now().toString()
                )
            )
            newsArticleRepository.saveAll(articles)
        }
    }

    @Test
    @DisplayName("Should return only test tech articles")
    fun `should filter news by category`() {
        val pageable = PageRequest.of(0, 10)
        val result = newsArticleRepository.findAllNewsByParams(pageable, "tech", "")

        val testResults = result.content.filter { it.uuid.startsWith("test-") }

        assertThat(testResults).hasSize(1)
        assertThat(testResults[0].categories).contains("tech")
    }

    @Test
    @DisplayName("Should filter by search term in title")
    fun `should filter news by search term`() {
        val pageable = PageRequest.of(0, 10)

        val result = newsArticleRepository.findAllNewsByParams(pageable, "", "business")

        val testResults = result.content.filter { it.uuid.startsWith("test-") }

        assertThat(testResults).hasSize(1)
        assertThat(testResults[0].title.lowercase()).contains("business")
    }

    @Test
    @DisplayName("Should return all articles when no filters are applied")
    fun `should return all news when no filters`() {
        val pageable = PageRequest.of(0, 10)
        val result = newsArticleRepository.findAllNewsByParams(pageable, "", "")

        val testResults = result.content.filter { it.uuid.startsWith("test-") }

        assertThat(testResults).hasSize(3)
    }
}