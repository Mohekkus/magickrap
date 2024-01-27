package http.certificate

import http.base.GenericRequest
import http.base.RequestsInterface
import http.certificate.model.request.AvailableServerRequest

class CertificateHandler {

    companion object {
        val instance = CertificateHandler()
        private val certificate = RequestsInterface.certificate
    }

    fun availableServer(requestBody: AvailableServerRequest? = null, onFinished: () -> Unit) {
        GenericRequest.runner(
            {
                certificate.getAvailableServer()
            },
            {

            }
        )
    }

}