package http.login

import http.APIClient
import http.login.model.request.CodeLoginRequest
import http.login.model.request.NormalLoginRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class LoginRequests {

    suspend fun normalLogin(username: String, password: String): HttpResponse {
        return APIClient.instance.post {
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

    suspend fun codeLogin(code: String): HttpResponse {
        return APIClient.instance.post {
            url {
                host = ""
                appendEncodedPathSegments("api", "auth", "code", "confirm-login-code")
            }
            setBody(
                CodeLoginRequest(
                    code = code
                )
            )
        }
    }
}