package http.login

import http.APIClient
import http.login.model.request.NormalLoginRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class LoginRequests {

    suspend fun normalLogin(username: String, password: String): HttpResponse {
        return APIClient.instance.post {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url {
                host = ""
                appendEncodedPathSegments("api", "auth", "login")
            }
            setBody(
                NormalLoginRequest(
                    identity = username,
                    password = password
                )
            )
        }
    }
}