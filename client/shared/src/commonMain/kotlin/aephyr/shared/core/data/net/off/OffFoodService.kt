package aephyr.shared.core.data.net.off

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import aephyr.shared.core.domain.port.FoodService
import aephyr.shared.core.domain.model.Food
import aephyr.shared.core.data.mapper.toDomain  // you will write this mapper
import aephyr.shared.core.domain.model.Nutrients

class OffFoodService(
    private val staging: Boolean = false // use world.openfoodfacts.net if prototyping
) : FoodService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 15_000
        }
        defaultRequest {
            header(HttpHeaders.UserAgent, "Aephyr/0.1 (your-app-name)")
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
    }

    override suspend fun byBarcode(code: String): Food? {
        val base = if (staging) "https://world.openfoodfacts.net" else "https://world.openfoodfacts.org"
        val url = URLBuilder(base)
            .appendPathSegments("api", "v2", "product", code)
            .build()
        val resp = client.get(url)
        return when (resp.status.value) {
            200 -> {
                val offResp = resp.body<OffProductResponse>()
                offResp.product?.let { off ->
                    off.nutriments?.let { nutr ->
                        Food(
                            id = off.code ?: code,
                            barcode = offResp.code,
                            name = off.productName ?: "Unknown",
                            brand = off.brands,
                            per100g = aephyr.shared.core.domain.model.Nutrients(
                                kcal = nutr.kcal ?: 0.0,
                                protein = nutr.protein100g ?: 0.0,
                                carbs = nutr.carbs100g ?: 0.0,
                                fat = nutr.fat100g ?: 0.0
                            ),
                            verified = (offResp.status == 1)
                        )
                    }
                }
            }
            404 -> null
            else -> error("OFF API returned status ${resp.status.value}: ${resp.bodyAsText()}")
        }
    }

    override suspend fun search(query: String): List<Food> {
        val base = "https://world.openfoodfacts.org/cgi/search.pl"
        val url = URLBuilder(base).apply {
            parameters.append("search_terms", query)
            parameters.append("search_simple", "1")
            parameters.append("action", "process")
            parameters.append("json", "1")
            parameters.append("page_size", "20")
            parameters.append("fields", "code,product_name,brands,nutriments")
        }.build()

        println("🔎 [OFF] v1 request: $url")
        val resp = client.get(url)
        if (resp.status.value != 200) error("OFF Search v1 ${resp.status}: ${resp.bodyAsText()}")

        val list = resp.body<OffSearchV1Response>().products

        return list.map { off ->
            val n = off.nutriments
            Food(
                id = off.code ?: "",
                barcode = off.code,
                name = off.productName?.ifBlank { "" } ?: "",
                brand = off.brands,
                per100g = aephyr.shared.core.domain.model.Nutrients(
                    kcal = n?.kcal ?: 0.0,
                    protein = n?.protein100g ?: 0.0,
                    carbs = n?.carbs100g ?: 0.0,
                    fat = n?.fat100g ?: 0.0
                ),
                verified = false
            )
        }.filter { it.name.isNotBlank() }
    }
}
