package http.certificate

import GeneratedCertificateResponse
import com.google.gson.Gson
import http.base.ClientModule
import http.base.response.ErrorMessages
import http.base.GenericHandler
import http.base.response.GenericModel
import http.base.wrapper.ResponseStatus
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.payload.GeneratePayload
import http.certificate.model.response.CertificateResponse
import http.certificate.model.response.ServerCertificateResponse

class CertificateHandler {

    companion object {
        val instance = CertificateHandler()
        private val certificate = ClientModule.instance.certificate
    }

    fun availableServer(
        requestBody: AvailableServerPayload? = null,
        onFailure: (String) -> Unit,
        onSuccess: (ServerCertificateResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                certificate.getAvailableServer( requestBody)
            },
            {
                when (it.status) {
                    ResponseStatus.SUCCESS ->
                        if (it.data == null)
                            onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                        else
                            Gson().fromJson(it.data.toString(), ServerCertificateResponse::class.java).apply {
                                onSuccess(this)
                            }

                    else ->
                        if (it.data == null)
                            onFailure(it.message ?: ErrorMessages.FAILED.value())
                        else
                            Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                                onFailure(meta?.message ?: ErrorMessages.FAILED.value())
                            }

                }
            }
        )
    }

    fun getCertificate(
        requestBody: CertificatePayload? = null,
        onFailure: (String) -> Unit,
        onSuccess: (CertificateResponse) -> Unit
    ) {
        GenericHandler.runner(
            {
                certificate.getCertificate( requestBody)
            }
        ) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    it.data?.let {
                        println("exist?")
                        Gson().fromJson(it.toString(), CertificateResponse::class.java)?.apply(onSuccess)
                    } ?: run {
                        println("Bonked?")
                        onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                    }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: ErrorMessages.FAILED.value())
                    else
                        Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                            onFailure(meta?.message ?: ErrorMessages.FAILED.value())
                        }

            }
        }
    }

    fun generateCertificate(
        protocol: String? = "openvpn_udp",
        servId: String,
        onFailure: (String) -> Unit,
        onSuccess: (GeneratedCertificateResponse) -> Unit
    ) {
        GenericHandler.runner({
            certificate.generateCertificate(
                GeneratePayload(protocol, servId)
            )
        }) {
            when (it.status) {
                ResponseStatus.SUCCESS ->
                    if (it.data == null)
                        onFailure(ErrorMessages.SUCCESS_NO_DATA.value())
                    else
                        Gson().fromJson(it.data.toString(), GeneratedCertificateResponse::class.java).apply {
                            onSuccess(this)
                        }

                else ->
                    if (it.data == null)
                        onFailure(it.message ?: ErrorMessages.FAILED.value())
                    else
                        Gson().fromJson(it.data.toString(), GenericModel::class.java).apply {
                            onFailure(meta?.message ?: ErrorMessages.FAILED.value())
                        }

            }
        }
    }
}