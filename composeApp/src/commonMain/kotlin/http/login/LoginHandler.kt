package http.login

import http.base.GenericRequest
import http.base.RequestsInterface
import http.login.model.request.QRLoginRequest

class LoginHandler {

    companion object {
        val instance = LoginHandler()
        private val login = RequestsInterface.login
    }

    fun login(username: String, password: String, onFinished :() -> Unit) {
        GenericRequest.runner(
            {
                login.normalLogin(username, password)
            },
            {

            }
        )
    }

    fun login(code: String, onFinished: () -> Unit) {
        GenericRequest.runner(
            {
                login.codeLogin(code)
            },
            {

            }
        )
    }

    fun login(qrBody: QRLoginRequest? = null, onFinished: () -> Unit) {
        GenericRequest.runner(
            {
                login.qrLogin(qrBody)
            },
            {

            }
        )
    }

}