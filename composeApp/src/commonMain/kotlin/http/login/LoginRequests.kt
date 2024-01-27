package http.login

import etc.Global.extend
import http.base.GenericRequest
import http.login.model.request.CodeLoginRequest
import http.login.model.request.NormalLoginRequest
import http.login.model.request.QRLoginRequest
import io.ktor.client.statement.*

class LoginRequests {

    companion object {
        val instance = LoginRequests()

        private val authPath = listOf("auth")
        private val codePath = authPath.extend(listOf("code"))
    }

    suspend fun normalLogin(username: String, password: String): HttpResponse {
        return GenericRequest.post(
            authPath.extend(
                listOf("login")
            ),
            NormalLoginRequest(
                identity = username,
                password = password
            )
        )
    }

    suspend fun codeLogin(code: String): HttpResponse {
        return GenericRequest.post(
            codePath.extend(
                listOf("code")
            ),
            CodeLoginRequest(
                code
            )
        )
    }

    suspend fun qrLogin(qrBody: QRLoginRequest? = null): HttpResponse {
        return GenericRequest.post(
            codePath.extend(
                listOf("generate-login-code")
            ),
            qrBody
        )
    }
}