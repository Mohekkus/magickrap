package http.login

import com.google.gson.Gson
import http.base.ClientModule
import http.base.ErrorMessages
import http.base.GenericHandler
import http.base.response.GenericModel
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
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                        else
                            Gson().fromJson(it.data.toString(), NormalLoginResponse::class.java).apply {
                                onSuccess(this)
                            }

                    else ->
                        if (it.data == null)
                            onFailure(it.message ?: ErrorMessages.FAILED.value())
                        else
                            Gson().fromJson(it.data.toString(), NormalLoginError::class.java).apply {
                                onFailure(data?.get(0) ?: ErrorMessages.FAILED.value())
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
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                        else
                            Gson().fromJson(it.data.toString(), CodeLoginResponse::class.java).apply {
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
        )
    }

    fun login(
        onSuccess: (QRLoginCreatedResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        GenericHandler.runner(
            {
                login.qrLogin()
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                        else
                            Gson().fromJson(it.data.toString(), QRLoginCreatedResponse::class.java).apply {
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
        )
    }

    fun login(
        provider: String,
        oAuth: OAuthPayload,
        onFailure: (String) -> Unit,
        onSuccess: (OAuth2LoginResponse) -> Unit
    ) {
        GenericHandler.runner({
            login.oauthLogin(provider, oAuth)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                    else
                        Gson().fromJson(it.data.toString(), OAuth2LoginResponse::class.java).apply {
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
}