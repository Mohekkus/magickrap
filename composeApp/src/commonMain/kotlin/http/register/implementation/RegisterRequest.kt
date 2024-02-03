package http.register.implementation

import http.base.GenericHandler
import http.register.module.RegisterPayload
import io.ktor.client.statement.*

class RegisterRequest: RegisterInterface {

    companion object {
        val instance = RegisterRequest()
        private val path = listOf("auth", "register")
    }

    override suspend fun registration(name: String, email: String, password: String, repassword: String): HttpResponse {
        return GenericHandler.post(
            path,
            RegisterPayload(
                name = name,
                email = email,
                password = password,
                passwordConfirmation = repassword
            )
        )
    }
}