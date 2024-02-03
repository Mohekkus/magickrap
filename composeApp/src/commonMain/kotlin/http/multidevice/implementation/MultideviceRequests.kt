package http.multidevice.implementation

import etc.Global.extend
import http.base.GenericHandler
import http.multidevice.model.request.AuthorizeDevicePayload
import io.ktor.client.statement.*

class MultideviceRequests: MultideviceInterface {
    companion object{
        val instance = MultideviceRequests()
        private val path = listOf("auth", "code")
    }

    override suspend fun codeLogin(token: String): HttpResponse {
        return GenericHandler.get(
            token,
            appendedPath = path.extend(
                listOf("confirm-login-code")
            )
        )
    }

    override suspend fun processLogin(token: String, encrypted: String, authorize: Boolean): HttpResponse {
        return GenericHandler.post(
            token,
            appendedPath = path.extend(
                listOf("authorizing-login-code")
            ),
            body = AuthorizeDevicePayload(
                encrypted = encrypted, authorize = authorize
            )
        )
    }
}