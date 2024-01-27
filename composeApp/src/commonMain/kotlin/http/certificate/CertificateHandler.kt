package http.certificate

import etc.Global.toDataClass
import http.base.GenericHandler
import http.base.RequestsInterface
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.response.AvailableCertificateError
import http.certificate.model.response.AvailableCertificateResponse
import http.certificate.model.response.CertificateResponse

class CertificateHandler {

    companion object {
        val instance = CertificateHandler()
        private val certificate = RequestsInterface.certificate
    }

    fun availableServer(requestBody: AvailableServerPayload? = null, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                certificate.getAvailableServer()
            },
            {
                val result = it.first.toDataClass(
                    if (it.second)
                        AvailableCertificateResponse::class.java
                    else
                        AvailableCertificateError::class.java
                )
            }
        )
    }

    fun getCertificate(requestBody: CertificatePayload? = null, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                certificate.getCertificate(requestBody)
            },
            {
                val result = it.first.toDataClass(
                    if (it.second)
                        CertificateResponse::class.java
                    else
                        AvailableCertificateError::class.java
                )
            }
        )

    }

}