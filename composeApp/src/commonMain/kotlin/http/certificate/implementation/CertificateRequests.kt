package http.certificate.implementation

import etc.Global.extend
import etc.Global.toMutableMap
import http.base.GenericHandler
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.payload.GeneratePayload
import io.ktor.client.statement.*

class CertificateRequests : CertificateInterface {

    companion object {
        val instance = CertificateRequests()

        private val certPath = listOf("certificate")
    }

    override suspend fun getAvailableServer(token: String, requestBody: AvailableServerPayload?): HttpResponse {
        return GenericHandler.get(
            token,
            certPath.extend(
                listOf("availserver")
            ),
            requestBody?.toMutableMap()
        )
    }

    override suspend fun getAvailableServer(requestBody: AvailableServerPayload?): HttpResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getCertificate(token: String, requestBody: CertificatePayload?): HttpResponse {
        return GenericHandler.get(
            token,
            certPath,
            requestBody?.toMutableMap()
        )
    }

    override suspend fun getCertificate(requestBody: CertificatePayload?): HttpResponse {
        TODO("Not yet implemented")
    }

    override suspend fun generateCertificate(token: String, requestBody: GeneratePayload?): HttpResponse {
        return GenericHandler.get(
            token,
            certPath.extend(
                listOf("generate")
            ),
            requestBody?.toMutableMap()
        )
    }
    override suspend fun generateCertificate(requestBody: GeneratePayload?): HttpResponse {
        TODO("Not yet implemented")
    }

}