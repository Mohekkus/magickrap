package logic

import CertificateDocument
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.ServerCertificateItem
import logic.CertificateLogic.CHECK
import logic.CertificateLogic.STATE
import storage.directories.ProtocolStorage
import vpn.VpnRunner

class ConnectionLogic {

    companion object {
        val instance = ConnectionLogic()
    }

    private val certificate = CertificateLogic.instance

    enum class STATUS {
        DISCONNECTED,
        EXTERNAL,
        INTERNAL,
        CONNECTED
    }

    fun start(
        server: ServerCertificateItem,
        protocol: ProtocolStorage.PROTOCOL,
        callback: (STATUS) -> Unit
    ) {
        callback(STATUS.EXTERNAL)
        certificate.apply {
            checkCertificate(server, protocol) { check ->
                when (check) {
                    CHECK.USABLE ->
                        certificate.getUsable()?.let {
                            startVPN(it, callback)
                        } ?: kotlin.run {
                            callback(STATUS.DISCONNECTED)
                        }
                    CHECK.WAIT ->
                        callback(STATUS.EXTERNAL)
                    else -> fetchCertificate(server, protocol) { state ->
                        if (state == STATE.FETCHED)
                            certificate.getFetched()?.let {
                                callback(STATUS.INTERNAL)
                                startVPN(it, callback)
                            } ?: kotlin.run {
                                callback(STATUS.DISCONNECTED)
                            }
                        else
                            callback(STATUS.DISCONNECTED)
                    }
                }
            }
        }

    }

    private fun startVPN(
        data: CertificateDocument,
        callback: (STATUS) -> Unit
    ) {
        VpnRunner.instance.start(data) {

        }
    }

    fun stop(callback: (STATUS) -> Unit) {
        VpnRunner.instance.terminate(
            certificate.getProtocol()
        )
        callback(STATUS.DISCONNECTED)
    }
}