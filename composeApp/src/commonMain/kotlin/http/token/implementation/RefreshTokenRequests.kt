package http.token.implementation

import http.base.GenericHandler
import io.ktor.client.statement.*

class RefreshTokenRequests: RefreshTokenInterface {

    companion object {
        val instance = RefreshTokenRequests()
        private val path = listOf("auth", "refresh-token")
    }

    override suspend fun refreshToken(token: String): HttpResponse {
        return GenericHandler.post(
            token,
            path
        )
    }
}