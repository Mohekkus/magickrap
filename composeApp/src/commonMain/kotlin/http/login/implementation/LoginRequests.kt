package http.login.implementation

import etc.Global.extend
import http.base.GenericHandler
import http.login.model.request.CodeLoginPayload
import http.login.model.request.NormalLoginPayload
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
            authPath.extend(
                listOf("login")
            ),
            NormalLoginPayload(
                identity = username,
                password = password
            )
        )
    }

    override suspend fun codeLogin(code: String): HttpResponse {
        return GenericHandler.post(
            codePath.extend(
                listOf("confirm-login-code")
            ),
            CodeLoginPayload(
                code
            )
        )
    }

    override suspend fun qrLogin(qrBody: QRLoginPayload?): HttpResponse {
        return GenericHandler.post(
            codePath.extend(
                listOf("generate-login-code")
            ),
            qrBody
        )
    }
}