package com.example.newsAggregator.servicesTests

import com.example.newsAggregator.controller.NewsController
import com.example.newsAggregator.services.NewsService
import com.example.newsAggregator.services.SourceService
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(NewsController::class)
class NewsControllerValidationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var newsService: NewsService

    @MockBean
    private lateinit var sourceService: SourceService

    @ParameterizedTest
    @CsvSource(
        "0,10,business,Page number must be greater than 0",
        "1,0,business,Page size must be at least 1",
        "1,101,business,Page size cannot be more than 100",
        "1,10,invalidCategory,Invalid category: invalidCategory"
    )
    fun `should return 400 BadRequest for invalid query params`(
        pageNumber: Int,
        pageSize: Int,
        category: String,
        expectedError: String
    ) {
        mockMvc.get("/api/news/article/page") {
            param("page_number", pageNumber.toString())
            param("page_size", pageSize.toString())
            param("category", category)
        }.andExpect {
            status { isBadRequest() }
            content { string(containsString(expectedError)) }
        }
    }
}