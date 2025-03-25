import com.example.newsAggregator.entity.NewsArticleDB

data class NewsArticleResponseBody(
    val newsArticle: List<NewsArticleDB>,
    val pageSizeLimit: Int
)