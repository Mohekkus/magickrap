package http.certificate

import GeneratedCertificateResponse
import com.google.gson.Gson
import http.base.ClientModule
import http.base.GenericHandler
import http.base.wrapper.ResponseStatus
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.payload.GeneratePayload
import http.certificate.model.response.CertificateResponse
import http.certificate.model.response.ServerCertificateResponse
import http.login.model.response.NormalLoginError

class CertificateHandler {

    companion object {
        val instance = CertificateHandler()
        private val certificate = ClientModule.instance.certificate
    }

    fun availableServer(token: String, requestBody: AvailableServerPayload? = null, onFinished: (Boolean, String?, ServerCertificateResponse?) -> Unit) {
        GenericHandler.runner(
            {
                certificate.getAvailableServer(token, requestBody)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS -> {
                        if (it.data == null) return@runner
                        Gson().fromJson(it.data.toString(), ServerCertificateResponse::class.java).apply {
                            onFinished(false, null, this)
                        }
                    }
                    else -> {
                        if (it.data == null) {
                            onFinished(true, it.message, null)
                            return@runner
                        }
                        Gson().fromJson(it.data.toString(), NormalLoginError::class.java).apply {
                            onFinished(true, data?.get(0), null)
                        }
                    }
                }
            }
        )
    }

    fun getCertificate(token: String, requestBody: CertificatePayload? = null, onFinished: (Boolean, String?, CertificateResponse?) -> Unit) {
        GenericHandler.runner(
            {
                certificate.getCertificate(token, requestBody)
            }
        ) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    if (it.data == null) return@runner
                    Gson().fromJson(it.data.toString(), CertificateResponse::class.java).apply {
                        onFinished(false, null, this)
                    }
                }
                else -> {
                    if (it.data == null) {
                        onFinished(true, it.message, null)
                        return@runner
                    }
                    Gson().fromJson(it.data.toString(), NormalLoginError::class.java).apply {
                        onFinished(true, data?.get(0), null)
                    }
                }
            }
        }
    }

    fun generateCertificate(token: String, protocol: String? = "openvpn_udp", servId: String, onFinished: (Boolean, String?, GeneratedCertificateResponse?) -> Unit) {
        GenericHandler.runner({
            certificate.generateCertificate(
                token,
                GeneratePayload(protocol, servId)
            )
        }) {

        }
    }
}