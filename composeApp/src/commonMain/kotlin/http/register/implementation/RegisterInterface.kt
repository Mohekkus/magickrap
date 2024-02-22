package http.register.implementation

import io.ktor.client.statement.*

interface RegisterInterface {
    suspend fun registration(name: String, email: String, password: String, repassword: String): HttpResponse
}