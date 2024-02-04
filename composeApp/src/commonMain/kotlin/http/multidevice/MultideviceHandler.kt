package http.multidevice

import com.google.gson.Gson
import http.base.ClientModule
import http.base.response.ErrorMessages
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.multidevice.model.response.AuthorizeDeviceResponse
import http.multidevice.model.response.GetAuthenticationCodeResponse
import http.multidevice.model.response.ListedDeviceResponse

class MultideviceHandler {
    companion object{
        val instance = MultideviceHandler()
        private val multidevice = ClientModule.instance.multidevice
    }

    fun generateCode(
        onFailure: (String) -> Unit,
        onSuccess: (GetAuthenticationCodeResponse) -> Unit
    ) {
        GenericHandler.runner({
            multidevice.codeLogin()
        }, {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                    else
                        Gson().fromJson(it.data.toString(), GetAuthenticationCodeResponse::class.java).apply {
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
        })
    }

    fun authorize(
        encrypted: String,
        authorize: Boolean,
        onFailure: (String) -> Unit,
        onSuccess: (AuthorizeDeviceResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                multidevice.processLogin(encrypted, authorize)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                        else
                            Gson().fromJson(it.data.toString(), AuthorizeDeviceResponse::class.java).apply {
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

    fun listedDevice(
        onFailure: (String) -> Unit,
        onSuccess: (ListedDeviceResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                multidevice.listedDevices()
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure("Succeeded, but no data available")
                        else
                            Gson().fromJson(it.data.toString(), ListedDeviceResponse::class.java).apply {
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

    fun revokeDevice(
        deviceId: String,
        onFailure: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        GenericHandler.runner(
            {
                multidevice.revokeDevice(deviceId)
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