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

    override suspend fun codeLogin(token: String): HttpResponse {
        return GenericHandler.get(
            token,
            appendedPath = code.extend(
                listOf("confirm-login-code")
            )
        )
    }

    override suspend fun processLogin(token: String, encrypted: String, authorize: Boolean): HttpResponse {
        return GenericHandler.post(
            token,
            appendedPath = code.extend(
                listOf("authorizing-login-code")
            ),
            body = AuthorizeDevicePayload(
                encrypted = encrypted, authorize = authorize
            )
        )
    }

    override suspend fun listedDevices(token: String): HttpResponse {
        return GenericHandler.get(
            token = token,
            appendedPath = devices
        )
    }

    override suspend fun revokeDevice(token: String, deviceId: String): HttpResponse {
        return GenericHandler.post(
            token = token,
            appendedPath = devices.extend(
                listOf("revoke")
            ),
            body = RevokeDevicePayload(
                listOf(deviceId)
            )
        )
    }
}