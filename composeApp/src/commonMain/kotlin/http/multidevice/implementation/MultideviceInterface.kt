package http.multidevice.implementation

import io.ktor.client.statement.*

interface MultideviceInterface {

    suspend fun codeLogin(): HttpResponse
    suspend fun processLogin(encrypted: String, authorize: Boolean): HttpResponse
    suspend fun listedDevices(): HttpResponse
    suspend fun revokeDevice(deviceId: String): HttpResponse
}