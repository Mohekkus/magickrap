package http.forgot

import com.google.gson.Gson
import http.base.ClientModule
import http.base.GenericHandler
import http.base.wrapper.ResponseStatus
import http.forgot.model.response.ForgotPasswordError
import http.forgot.model.response.OTPForgotPasswordResponse
import http.forgot.model.response.ResetPasswordResponse
import http.login.model.response.NormalLoginError
import http.login.model.response.NormalLoginResponse

class ForgotPasswordHandler {

    companion object {
        val instance = ForgotPasswordHandler()
        val forgot = ClientModule.instance.forgot
    }

    fun forgotPassword(identity: String, provider: String, onFinished: (Boolean, String?) -> Unit) {
        GenericHandler.runner({
            forgot.forgotPassword(identity, provider)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    if (it.data == null)
                        onFinished(true, "No data returned, weird")
                    else
                        Gson().fromJson(it.data.toString(), GenericHandler::class.java).apply {
                            onFinished(false, null)
                        }
                }
                else -> {
                    if (it.data == null) {
                        onFinished(true, it.message)
                        return@runner
                    }
                    Gson().fromJson(it.data.toString(), ForgotPasswordError::class.java).apply {
                        onFinished(true, message)
                    }
                }
            }
        }
    }

    fun otpForgotPassword(identity: String, token: String, onFinished: (Boolean, String?, OTPForgotPasswordResponse?) -> Unit) {
        GenericHandler.runner({
            forgot.otpForgotPassword(identity, token)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    if (it.data == null)
                        onFinished(true, "No data returned, weird", null)
                    else
                        Gson().fromJson(it.data.toString(), OTPForgotPasswordResponse::class.java).apply {
                            onFinished(false, null, this)
                        }
                }
                else -> {
                    if (it.data == null) {
                        onFinished(true, it.message, null)
                        return@runner
                    }
                    Gson().fromJson(it.data.toString(), NormalLoginError::class.java).apply {
                        onFinished(true, data?.get(0), null)
                    }
                }
            }
        }
    }

    fun resetPassword(token: String, password: String, repassword: String, identity: String, onFinished: (Boolean, String?, ResetPasswordResponse?) -> Unit) {
        GenericHandler.runner({
            forgot.resetPassword(token, password, repassword, identity)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    if (it.data == null)
                        onFinished(true, "No data returned, weird", null)
                    else
                        Gson().fromJson(it.data.toString(), ResetPasswordResponse::class.java).apply {
                            onFinished(false, null, this)
                        }
                }
                else -> {
                    if (it.data == null) {
                        onFinished(true, it.message, null)
                        return@runner
                    }
                    Gson().fromJson(it.data.toString(), NormalLoginError::class.java).apply {
                        onFinished(true, data?.get(0), null)
                    }
                }
            }
        }
    }

}