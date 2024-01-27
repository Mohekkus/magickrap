package http.login

import etc.Global.toDataClass
import http.base.GenericHandler
import http.base.RequestsInterface
import http.login.model.request.QRLoginPayload
import http.login.model.response.*

class LoginHandler {

    companion object {
        val instance = LoginHandler()
        private val login = RequestsInterface.login
    }

    fun login(username: String, password: String, onFinished : () -> Unit) {
        GenericHandler.runner(
            {
                login.normalLogin(username, password)
            },
            {
                val result = it.first.toDataClass(
                    if (it.second)
                        NormalLoginResponse::class.java
                    else
                        NormalLoginError::class.java
                )
            }
        )
    }

    fun login(code: String, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                login.codeLogin(code)
            },
            {
                val result = it.first.toDataClass(
                    if (it.second)
                        CodeLoginResponse::class.java
                    else
                        CodeLoginError::class.java
                )
            }
        )
    }

    fun login(qrBody: QRLoginPayload? = null, isCreating: Boolean? = false, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                login.qrLogin(qrBody)
            },
            {
                val result = it.first.toDataClass(
                    if (isCreating == true)
                        QRLoginCreatedResponse::class.java
                    else
                        QRLoginSuccessResponse::class.java
                )
            }
        )
    }

}