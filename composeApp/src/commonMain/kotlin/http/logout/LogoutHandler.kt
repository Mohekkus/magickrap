package http.logout

import com.google.gson.Gson
import http.base.ClientModule
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.logout.implementation.LogoutRequest

class LogoutHandler {

    companion object {
        val instance = LogoutRequest()
        private val logout = ClientModule.instance.logout
    }

    fun logout(
        token: String,
        onFailure: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        GenericHandler.runner(
            {
                logout.attempt(token)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure("Succeeded, but no data available")
                        else
                            onSuccess()

                    else ->
                        if (it.data == null)
                            onFailure(it.message ?: "There is no error messages available")
                        else
                            Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                                onFailure(meta?.message ?: "There is no error messages available")
                            }

                }
            }
        )
    }
}