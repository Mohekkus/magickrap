package http

import androidx.compose.ui.graphics.vector.addPathNodes
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import java.text.DateFormat

object APIClient {

    val instance = HttpClient(Java) {
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        install(DefaultRequest) {
            host = "api-staging.auxonode.com"
            url {
                protocol = URLProtocol.HTTPS
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Any)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}