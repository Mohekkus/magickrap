package http.login

import http.base.ClientModule
import http.base.GenericHandler
import http.base.response.ResponseStatus
import http.login.model.request.QRLoginPayload

class LoginHandler {

    companion object {
        val instance = LoginHandler()
        private val login = ClientModule.instance.login
    }

    fun nlogin(username: String, password: String) {
        GenericHandler.runner(
            {
                login.normalLogin(username, password)
            },
            {
            }
        )
    }

    fun clogin(code: String, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                login.codeLogin(code)
            },
            {
            }
        )
    }

    fun qlogin(qrBody: QRLoginPayload? = null, isCreating: Boolean? = false, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                login.qrLogin(qrBody)
            },
            {
            }
        )
    }

}