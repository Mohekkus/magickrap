package http.certificate

import http.base.ClientModule
import http.base.GenericHandler
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.payload.GeneratePayload
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CertificateHandler {

    companion object {
        val instance = CertificateHandler()
        private val certificate = ClientModule.instance.certificate
    }

    fun availableServer(requestBody: AvailableServerPayload? = null, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                certificate.getAvailableServer(requestBody)
            },
            {

            }
        )
    }

    fun getCertificate(requestBody: CertificatePayload? = null, onFinished: () -> Unit) {
        GenericHandler.runner(
            {
                certificate.getCertificate(requestBody)
            }
        ) {

        }
    }

    fun generateCertificate(protocol: String, servId: String) {
        GenericHandler.runner({
            certificate.generateCertificate(
                GeneratePayload(protocol, servId)
            )
        }) {

        }
    }
}