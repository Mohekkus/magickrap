package http.logout.implementation

import io.ktor.client.statement.*

interface LogoutInterface {

    suspend fun attempt(token: String): HttpResponse
}