package aephyr.shared.data.net

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

class ApiClient(private val baseUrl: String) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis  = 15_000
        }
        install(Logging) { level = LogLevel.ALL }
    }

    /** Callback wrapper for simple Swift interop */
    fun health(callback: (String?, Throwable?) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val text: String = client.get("$baseUrl/api/health") {
                    accept(ContentType.Text.Plain)
                }.body()
                callback(text, null)
            } catch (t: Throwable) {
                callback(null, t)
            }
        }
    }
}
