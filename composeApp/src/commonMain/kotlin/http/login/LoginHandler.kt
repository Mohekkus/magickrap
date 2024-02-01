package http.login

import com.google.gson.Gson
import http.base.ClientModule
import http.base.GenericHandler
import http.base.wrapper.ResponseStatus
import http.login.model.request.OAuthPayload
import http.login.model.request.QRLoginPayload
import http.login.model.response.*

class LoginHandler {

    companion object {
        val instance = LoginHandler()
        private val login = ClientModule.instance.login
    }

    fun login(
        username: String,
        password: String,
        onSuccess: (NormalLoginResponse) -> Unit,
        onFailure: (String) -> Unit
        ) {
        GenericHandler.runner(
            {
                login.normalLogin(username, password)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS -> {
                        if (it.data == null)
                            onFailure("Succeeded, but no data available")
                        else
                            Gson().fromJson(it.data.toString(), NormalLoginResponse::class.java).apply {
                                onSuccess(this)
                            }
                    }
                    else -> {
                        if (it.data == null)
                            onFailure(it.message ?: "There is no error messages available")
                        else
                            Gson().fromJson(it.data.toString(), NormalLoginError::class.java).apply {
                                onFailure(data?.get(0) ?: "There is no error messages available")
                            }
                    }
                }
            }
        )
    }

    fun login(
        code: String,
        onSuccess: (CodeLoginResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        GenericHandler.runner(
            {
                login.codeLogin(code)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS -> {
                        if (it.data == null)
                            onFailure("Succeeded, but no data available")
                        else
                            Gson().fromJson(it.data.toString(), CodeLoginResponse::class.java).apply {
                                onSuccess(this)
                            }
                    }
                    else -> {
                        if (it.data == null)
                            onFailure(it.message ?: "There is no error messages available")
                        else
                            Gson().fromJson(it.data.toString(), CodeLoginError::class.java).apply {
                                onFailure(meta?.message ?: "There is no error messages available")
                            }
                    }
                }
            }
        )
    }

    fun login(
        qrBody: QRLoginPayload? = null,
        onSuccess: (QRLoginCreatedResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        GenericHandler.runner(
            {
                login.qrLogin(qrBody)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS -> {
                        if (it.data == null)
                            onFailure("Succeeded, but no data available")
                        else
                            Gson().fromJson(it.data.toString(), QRLoginCreatedResponse::class.java).apply {
                                onSuccess(this)
                            }
                    }
                    else -> {
                        if (it.data == null)
                            onFailure(it.message ?: "There is no error messages available")
                        else
                            Gson().fromJson(it.data.toString(), CodeLoginError::class.java).apply {
                                onFailure(meta?.message ?: "There is no error messages available")
                            }
                    }
                }
            }
        )
    }

    fun login(provider: String, oAuth: OAuthPayload, onFinished: (Boolean, String?, OAuth2LoginResponse?) -> Unit) {
        GenericHandler.runner({
            login.oauthLogin(provider, oAuth)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    if (it.data == null) return@runner

                    Gson().fromJson(it.data.toString(), OAuth2LoginResponse::class.java).apply {
                        onFinished(false, null, this)
                    }
                }
                else -> {
                    if (it.data == null) {
                        onFinished(true, it.message, null)
                        return@runner
                    }
                    Gson().fromJson(it.data.toString(), CodeLoginError::class.java).apply {
                        onFinished(true, meta?.message.toString(), null)
                    }
                }
            }
        }
    }
}