import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.HttpRequest
import org.slf4j.LoggerFactory

class LoggingInterceptor : ClientHttpRequestInterceptor {

    private val logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): org.springframework.http.client.ClientHttpResponse {

        logger.info("Request: ${request.method} ${request.uri}")
        logger.info("Request body: ${String(body)}")

        val response = execution.execute(request, body)
        val responseBody = response.body.bufferedReader().readText()

        logger.info("Response status: ${response.statusCode}")
        logger.info("Response body: $responseBody")

        return response
    }
}