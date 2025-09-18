package aephyr.shared.core.data.net.http

import aephyr.shared.core.data.net.dto.FoodDto
import aephyr.shared.core.data.mapper.toDomain
import aephyr.shared.core.domain.model.Food
import aephyr.shared.core.domain.port.FoodService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpFoodService(
    private val baseUrl: String,
    private val userAgent: String = "Aephyr/0.1 (iOS; KMM)"
) : FoodService {

    private val client = HttpClient {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis  = 15_000
        }
        defaultRequest {
            header(HttpHeaders.UserAgent, userAgent)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
    }

    override suspend fun byBarcode(code: String): Food? {
        val url = URLBuilder(baseUrl).appendPathSegments("v1","foods","barcode", code).build()
        val resp = client.get(url)
        return when (resp.status) {
            HttpStatusCode.OK -> resp.body<FoodDto>().toDomain()
            HttpStatusCode.NotFound -> null
            else -> error("API ${resp.status}: ${resp.bodyAsText()}")
        }
    }

    override suspend fun search(query: String): List<Food> {
        val url = URLBuilder(baseUrl).appendPathSegments("v1","foods","search")
            .apply { parameters.append("q", query);  }
            .build()
        val resp = client.get(url)
        return when (resp.status) {
            HttpStatusCode.OK -> resp.body<List<FoodDto>>().map { it.toDomain() }
            else -> error("API ${resp.status}: ${resp.bodyAsText()}")
        }
    }
}
