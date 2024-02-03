package http.multidevice.implementation

import io.ktor.client.statement.*

interface MultideviceInterface {

    suspend fun codeLogin(token: String): HttpResponse
    suspend fun processLogin(token: String, encrypted: String, authorize: Boolean): HttpResponse
}