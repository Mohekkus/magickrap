package http.register

import com.google.gson.Gson
import http.base.ClientModule
import http.base.response.ErrorMessages
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.register.module.RegisterPayload

class RegisterHandler {
    companion object {
        val instance = RegisterHandler()
        private val register = ClientModule.instance.register
    }

    fun registration(
        name: String,
        email: String,
        password: String,
        repassword: String,
        onFailure: (String) -> Unit,
        onSuccess: (RegisterPayload) -> Unit
    ) {
        GenericHandler.runner(
            {
                register.registration(name, email, password, repassword)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                        else
                            Gson().fromJson(it.data.toString(), RegisterPayload::class.java).apply {
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
        )
    }
}