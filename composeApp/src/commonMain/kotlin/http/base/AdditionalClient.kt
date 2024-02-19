package http.base

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import java.text.DateFormat

class AdditionalClient {

    companion object {
        val instance = AdditionalClient()
    }

    fun client(url: String): HttpClient {
        return HttpClient(Java) {
            install(HttpTimeout)
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                gson {
                    setDateFormat(DateFormat.LONG)
                    setPrettyPrinting()
                }
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                accept(ContentType.Application.Any)
                url(host = url) {
                    protocol = URLProtocol.HTTPS
                    build()
                }
            }
        }
    }
}