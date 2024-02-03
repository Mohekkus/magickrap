package http.multidevice.implementation

import etc.Global.extend
import http.base.GenericHandler
import io.ktor.client.statement.*

class MultideviceRequests: MultideviceInterface {
    companion object{
        val instance = MultideviceRequests()
        private val path = listOf("auth")
    }

    override suspend fun codeLogin(token: String): HttpResponse {
        return GenericHandler.get(
            token,
            appendedPath = path.extend(
                listOf("code", "confirm-login-code")
            )
        )
    }
}