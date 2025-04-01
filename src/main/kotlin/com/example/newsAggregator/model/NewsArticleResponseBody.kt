import com.example.newsAggregator.model.NewsArticle

data class NewsArticleResponseBody(
    val newsArticle: List<NewsArticle>,
    val pageSizeLimit: Int?
)