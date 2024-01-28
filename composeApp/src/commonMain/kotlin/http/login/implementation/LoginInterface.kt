package http.login.implementation

import http.login.model.request.QRLoginPayload
import io.ktor.client.statement.*

interface LoginInterface {
    suspend fun normalLogin(username: String, password: String): HttpResponse
    suspend fun codeLogin(code: String): HttpResponse
    suspend fun qrLogin(qrBody: QRLoginPayload? = null): HttpResponse
}