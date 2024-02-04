package http.certificate.implementation

import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.payload.GeneratePayload
import io.ktor.client.statement.*

interface CertificateInterface {
    suspend fun getAvailableServer(requestBody: AvailableServerPayload? = null): HttpResponse
    suspend fun getCertificate(requestBody: CertificatePayload? = null): HttpResponse
    suspend fun generateCertificate(requestBody: GeneratePayload? = null): HttpResponse
}