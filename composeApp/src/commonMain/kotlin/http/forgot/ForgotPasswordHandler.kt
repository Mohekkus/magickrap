package http.forgot

import com.google.gson.Gson
import http.base.ClientModule
import http.base.response.ErrorMessages
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.forgot.model.response.ForgotPasswordError
import http.forgot.model.response.OTPForgotPasswordResponse
import http.forgot.model.response.ResetPasswordResponse
import http.login.model.response.CodeLoginError

class ForgotPasswordHandler {

    companion object {
        val instance = ForgotPasswordHandler()
        val forgot = ClientModule.instance.forgot
    }

    fun forgotPassword(
        identity: String,
        provider: String,
        onFailure: (String) -> Unit,
        onSuccess: () -> Unit) {
        GenericHandler.runner({
            forgot.forgotPassword(identity, provider)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                    else
                        onSuccess()

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: ErrorMessages.FAILED.value())
                    else
                        Gson().fromJson(it.data.toString(), ForgotPasswordError::class.java).apply {
                            onFailure(message ?: ErrorMessages.FAILED.value())
                        }

            }
        }
    }

    fun otpForgotPassword(
        identity: String,
        token: String,
        onFailure: (String) -> Unit,
        onSuccess: (OTPForgotPasswordResponse) -> Unit
    ) {
        GenericHandler.runner({
            forgot.otpForgotPassword(identity, token)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                    else
                        Gson().fromJson(it.data.toString(), OTPForgotPasswordResponse::class.java).apply {
                            onSuccess(this)
                        }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: ErrorMessages.FAILED.value())
                    else
                        Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                            onFailure(meta?.message ?: ErrorMessages.FAILED.value())
                        }

            }
        }
    }

    fun resetPassword(
        token: String,
        password: String,
        repassword: String,
        identity: String,
        onFailure: (String) -> Unit,
        onSuccess: (ResetPasswordResponse) -> Unit
    ) {
        GenericHandler.runner({
            forgot.resetPassword(token, password, repassword, identity)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                    else
                        Gson().fromJson(it.data.toString(), ResetPasswordResponse::class.java).apply {
                            onSuccess(this)
                        }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: ErrorMessages.FAILED.value())
                    else
                        Gson().fromJson(it.data.toString(), CodeLoginError::class.java).apply {
                            onFailure(meta?.message ?: ErrorMessages.FAILED.value())
                        }
            }
        }
    }

}