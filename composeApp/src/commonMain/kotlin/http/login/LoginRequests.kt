package http.login

import etc.Global.extend
import http.base.GenericHandler
import http.login.model.request.CodeLoginPayload
import http.login.model.request.NormalLoginPayload
import http.login.model.request.QRLoginPayload
import io.ktor.client.statement.*

class LoginRequests {

    companion object {
        val instance = LoginRequests()

        private val authPath = listOf("auth")
        private val codePath = authPath.extend(listOf("code"))
    }

    suspend fun normalLogin(username: String, password: String): HttpResponse {
        return GenericHandler.post(
            authPath.extend(
                listOf("login")
            ),
            NormalLoginPayload(
                identity = username,
                password = password
            )
        )
    }

    suspend fun codeLogin(code: String): HttpResponse {
        return GenericHandler.post(
            codePath.extend(
                listOf("code")
            ),
            CodeLoginPayload(
                code
            )
        )
    }

    suspend fun qrLogin(qrBody: QRLoginPayload? = null): HttpResponse {
        return GenericHandler.post(
            codePath.extend(
                listOf("generate-login-code")
            ),
            qrBody
        )
    }
}