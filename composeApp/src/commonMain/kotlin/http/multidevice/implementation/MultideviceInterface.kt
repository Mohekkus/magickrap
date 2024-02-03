package http.multidevice.implementation

import io.ktor.client.statement.*

interface MultideviceInterface {

    suspend fun codeLogin(token: String): HttpResponse
}