package http.token.implementation

import io.ktor.client.statement.*

interface RefreshTokenInterface {

    suspend fun refreshToken(): HttpResponse
}