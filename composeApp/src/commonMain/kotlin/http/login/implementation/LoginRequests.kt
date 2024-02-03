package http.login.implementation

import etc.Global.extend
import http.base.GenericHandler
import http.login.model.request.CodeLoginPayload
import http.login.model.request.NormalLoginPayload
import http.login.model.request.OAuthPayload
import http.login.model.request.QRLoginPayload
import io.ktor.client.statement.*

class LoginRequests : LoginInterface {

    companion object {
        val instance = LoginRequests()

        private val authPath = listOf("auth")
        private val codePath = authPath.extend(listOf("code"))
    }

    override suspend fun normalLogin(username: String, password: String): HttpResponse {
        return GenericHandler.post(
            appendedPath = authPath.extend(
                listOf("login")
            ),
            body = NormalLoginPayload(
                identity = username,
                password = password
            )
        )
    }

    override suspend fun codeLogin(code: String): HttpResponse {
        return GenericHandler.post(
            appendedPath = codePath.extend(
                listOf("confirm-login-code")
            ),
            body = CodeLoginPayload(
                code
            )
        )
    }

    override suspend fun qrLogin(): HttpResponse {
        return GenericHandler.post(
            appendedPath = codePath.extend(
                listOf("generate-login-code")
            ),
            body = QRLoginPayload()
        )
    }

    override suspend fun oauthLogin(provider: String, body: OAuthPayload): HttpResponse {
        return GenericHandler.post(
            appendedPath = authPath.extend(
                listOf("oauth2", provider)
            ),
            body = body
        )
    }
}