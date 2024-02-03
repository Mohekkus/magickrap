package http.base

import http.certificate.implementation.CertificateRequests
import http.forgot.implementation.ForgotPasswordRequests
import http.login.implementation.LoginRequests
import http.logout.implementation.LogoutRequest
import http.register.implementation.RegisterRequest
import http.token.implementation.RefreshTokenRequests
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import java.text.DateFormat

class ClientModule {

    companion object {
        val instance = ClientModule()
    }

    val client = HttpClient(Java) {
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
        install(HttpTimeout)
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    val login = LoginRequests.instance
    val certificate = CertificateRequests.instance
    val forgot = ForgotPasswordRequests.instance
    val refresh = RefreshTokenRequests.instance
    val logout = LogoutRequest.instance
    val register = RegisterRequest.instance
}