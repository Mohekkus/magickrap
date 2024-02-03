package http.logout.implementation

import http.base.GenericHandler
import io.ktor.client.statement.*

class LogoutRequest: LogoutInterface {
    companion object {
        val instance = LogoutRequest()
        private val path = listOf("auth", "logout")
    }

    override suspend fun attempt(token: String): HttpResponse {
        return GenericHandler.post(
            path
        )
    }
}