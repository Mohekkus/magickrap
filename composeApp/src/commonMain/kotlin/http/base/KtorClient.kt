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

object KtorClient {

    val instance = HttpClient(Java) {
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        install(DefaultRequest) {
            url(host= "api-staging.auxonode.com/api") {
                protocol = URLProtocol.HTTPS
                build()
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Any)

            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}