package http.certificate

import etc.Global.extend
import http.base.GenericHandler
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import io.ktor.client.statement.*

class CertificateRequests {

    companion object {
        val instance = CertificateRequests()

        private val certPath = listOf("certificate")
    }

    suspend fun getAvailableServer(requestBody: AvailableServerPayload? = null): HttpResponse {
        return GenericHandler.post(
            certPath.extend(
                listOf("availserver")
            ),
            requestBody
        )
    }

    suspend fun getCertificate(requestBody: CertificatePayload? = null): HttpResponse {
        return GenericHandler.post(
            certPath,
            requestBody
        )
    }
}