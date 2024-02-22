package http.forgot.implementation

import io.ktor.client.statement.*

interface ForgotPasswordInterface {

    suspend fun forgotPassword(identity: String, provider: String): HttpResponse
    suspend fun otpForgotPassword(identity: String, token: String): HttpResponse
    suspend fun resetPassword(token: String, password: String, repassword: String, identity: String): HttpResponse
}