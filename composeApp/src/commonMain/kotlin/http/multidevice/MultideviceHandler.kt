package http.multidevice

import com.google.gson.Gson
import http.base.ClientModule
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.multidevice.model.response.AuthorizeDeviceResponse
import http.multidevice.model.response.GetAuthenticationCodeResponse

class MultideviceHandler {
    companion object{
        val instance = MultideviceHandler()
        private val multidevice = ClientModule.instance.multidevice
    }

    fun generateCode(
        token: String,
        onFailure: (String) -> Unit,
        onSuccess: (GetAuthenticationCodeResponse) -> Unit
    ) {
        GenericHandler.runner({
            multidevice.codeLogin(token)
        }, {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure("Succeeded, but no data available")
                    else
                        Gson().fromJson(it.data.toString(), GetAuthenticationCodeResponse::class.java).apply {
                            onSuccess(this)
                        }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: "There is no error messages available")
                    else
                        Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                            onFailure(meta?.message ?: "There is no error messages available")
                        }

            }
        })
    }

    fun authorize(
        token: String,
        encrypted: String,
        authorize: Boolean,
        onFailure: (String) -> Unit,
        onSuccess: (AuthorizeDeviceResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                multidevice.processLogin(token, encrypted, authorize)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure("Succeeded, but no data available")
                        else
                            Gson().fromJson(it.data.toString(), AuthorizeDeviceResponse::class.java).apply {
                                onSuccess(this)
                            }

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