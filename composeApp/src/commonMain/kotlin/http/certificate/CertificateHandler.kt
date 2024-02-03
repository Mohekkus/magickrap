package http.certificate

import GeneratedCertificateResponse
import com.google.gson.Gson
import http.base.ClientModule
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.payload.GeneratePayload
import http.certificate.model.response.CertificateResponse
import http.certificate.model.response.ServerCertificateResponse
import http.login.model.response.NormalLoginError
import http.login.model.response.NormalLoginResponse

class CertificateHandler {

    companion object {
        val instance = CertificateHandler()
        private val certificate = ClientModule.instance.certificate
    }

    fun availableServer(
        token: String,
        requestBody: AvailableServerPayload? = null,
        onFailure: (String) -> Unit,
        onSuccess: (ServerCertificateResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                certificate.getAvailableServer(token, requestBody)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure("Succeeded, but no data available")
                        else
                            Gson().fromJson(it.data.toString(), ServerCertificateResponse::class.java).apply {
                                onSuccess(this)
                            }

                    else ->
                        if (it.data == null)
                            onFailure(it.message ?: "There is no error messages available")
                        else
                            Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                                onFailure(meta?.message ?: "There is no error messages available")
                            }

                }
            }
        )
    }

    fun getCertificate(
        token: String,
        requestBody: CertificatePayload? = null,
        onFailure: (String) -> Unit,
        onSuccess: (CertificateResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                certificate.getCertificate(token, requestBody)
            }
        ) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure("Succeeded, but no data available")
                    else
                        Gson().fromJson(it.data.toString(), CertificateResponse::class.java).apply {
                            onSuccess(this)
                        }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: "There is no error messages available")
                    else
                        Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                            onFailure(meta?.message ?: "There is no error messages available")
                        }

            }
        }
    }

    fun generateCertificate(
        token: String,
        protocol: String? = "openvpn_udp",
        servId: String,
        onFailure: (String) -> Unit,
        onSuccess: (GeneratedCertificateResponse) -> Unit
    ) {
        GenericHandler.runner({
            certificate.generateCertificate(
                token,
                GeneratePayload(protocol, servId)
            )
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure("Succeeded, but no data available")
                    else
                        Gson().fromJson(it.data.toString(), GeneratedCertificateResponse::class.java).apply {
                            onSuccess(this)
                        }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: "There is no error messages available")
                    else
                        Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                            onFailure(meta?.message ?: "There is no error messages available")
                        }

            }
        }
    }
}