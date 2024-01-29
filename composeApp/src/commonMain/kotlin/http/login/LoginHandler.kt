package http.login

import http.base.ClientModule
import http.base.GenericHandler
import http.base.response.ResponseStatus
import http.login.model.request.OAuthPayload
import http.login.model.request.QRLoginPayload
import http.login.model.response.*

class LoginHandler {

    companion object {
        val instance = LoginHandler()
        private val login = ClientModule.instance.login
    }

    fun login(username: String, password: String) {
        GenericHandler.runner(
            {
                login.normalLogin(username, password)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data is NormalLoginResponse) {
                            val response = it.data
                        }
                        else {}
                    ResponseStatus.ERROR ->
                        if (it.data is NormalLoginError) {
                            val error = it.data
                        }
                        else {}
                }
            }
        )
    }

    fun login(code: String, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                login.codeLogin(code)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data is CodeLoginResponse) {
                            val response = it.data
                        }
                        else {}
                    ResponseStatus.ERROR ->
                        if (it.data is CodeLoginError) {
                            val error = it.data
                        } else {}
                }
            }
        )
    }

    fun login(qrBody: QRLoginPayload? = null, isCreating: Boolean? = false, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                login.qrLogin(qrBody)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data is QRLoginCreatedResponse) {
                            val response = it.data
                        }
                        else if (it.data is QRLoginSuccessResponse) {
                            val response = it.data
                        }
                        else {}
                    ResponseStatus.ERROR ->
                        if (it.data is NormalLoginError) {
                            val error = it.data
                        } else {}
                }
            }
        )
    }

    fun login(provider: String, oAuth: OAuthPayload) {
        GenericHandler.runner({
            login.oauthLogin(provider, oAuth)
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data is OAuth2LoginResponse) {
                        val response: OAuth2LoginResponse = it.data
                    }
                    else {}
                ResponseStatus.ERROR ->
                    if (it.data is CodeLoginError) {
                        val error: CodeLoginError = it.data
                    } else {}
            }
        }
    }

}