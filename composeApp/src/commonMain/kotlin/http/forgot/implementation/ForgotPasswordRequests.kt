package http.forgot.implementation

import etc.Global.extend
import http.base.GenericHandler
import http.forgot.model.request.ForgotPasswordPayload
import http.forgot.model.request.OTPForgotPasswordPayload
import http.forgot.model.request.ResetPasswordPayload
import io.ktor.client.statement.*

class ForgotPasswordRequests: ForgotPasswordInterface {

    companion object {
        val instance = ForgotPasswordRequests()
        private val path = listOf("auth")
    }

    override suspend fun forgotPassword(identity: String, provider: String): HttpResponse {
        return GenericHandler.post(
            path.extend(
                listOf("forgot-password")
            ),
            ForgotPasswordPayload(
                identity, provider
            )
        )
    }

    override suspend fun otpForgotPassword(identity: String, token: String): HttpResponse {
        return GenericHandler.post(
            path.extend(
                listOf("forgot-password-otp")
            ),
            OTPForgotPasswordPayload(
                identity, token
            )
        )
    }

    override suspend fun resetPassword(
        token: String,
        password: String,
        repassword: String,
        identity: String
    ): HttpResponse {
        return GenericHandler.post(
            path.extend(
                listOf("reset-password")
            ),
            ResetPasswordPayload(
                token, password, repassword, identity
            )
        )
    }

}