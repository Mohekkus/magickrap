package http.multidevice.implementation

import etc.Global.extend
import http.base.GenericHandler
import http.multidevice.model.request.AuthorizeDevicePayload
import http.multidevice.model.request.RevokeDevicePayload
import io.ktor.client.statement.*

class MultideviceRequests: MultideviceInterface {
    companion object{
        val instance = MultideviceRequests()
        private val code = listOf("auth", "code")
        private val devices = listOf("profile", "connected-devices")
    }

    override suspend fun codeLogin(): HttpResponse {
        return GenericHandler.get(
            appendedPath = code.extend(
                listOf("confirm-login-code")
            )
        )
    }

    override suspend fun processLogin(encrypted: String, authorize: Boolean): HttpResponse {
        return GenericHandler.post(
            appendedPath = code.extend(
                listOf("authorizing-login-code")
            ),
            body = AuthorizeDevicePayload(
                encrypted = encrypted, authorize = authorize
            )
        )
    }

    override suspend fun listedDevices(): HttpResponse {
        return GenericHandler.get(
            appendedPath = devices
        )
    }

    override suspend fun revokeDevice(deviceId: String): HttpResponse {
        return GenericHandler.post(
            appendedPath = devices.extend(
                listOf("revoke")
            ),
            body = RevokeDevicePayload(
                listOf(deviceId)
            )
        )
    }
}