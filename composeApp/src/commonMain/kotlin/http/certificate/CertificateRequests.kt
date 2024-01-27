package http.certificate

import etc.Global.extend
import http.base.GenericRequest
import http.base.KtorClient
import http.certificate.model.request.AvailableServerRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class CertificateRequests {

    companion object {
        val instance = CertificateRequests()

        private val certPath = listOf("certificate")
    }

    suspend fun getAvailableServer(requestBody: AvailableServerRequest? = null): HttpResponse {
        return GenericRequest.post(
            certPath.extend(
                listOf("availserver")
            ),
            requestBody
        )
    }
}