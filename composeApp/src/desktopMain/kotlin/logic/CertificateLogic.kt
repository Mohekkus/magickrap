package logic

import CertificateDocument
import GeneratedCertificateResponse
import http.ApiHandler
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.response.CertificateResponse
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import storage.directories.CertificateStorage
import storage.directories.ProtocolStorage
import storage.directories.ServerStorage

class CertificateLogic {

    companion object {
        val instance = CertificateLogic()
    }

    private val mServer = ServerStorage.instance
    private val mProtocol = ProtocolStorage.instance
    private val mCertificate = CertificateStorage.instance

    private var fetched: CertificateDocument? = null

    private var state =
        STATE.IDLE

    enum class CHECK {
        EMPTY,
        USABLE,
        DIFFERENT,
        WAIT,
        ERROR
    }

    enum class STATE {
        RETRY,
        GET,
        GENERATE,
        FETCHED,
        FAIL,
        IDLE
    }

    private fun save(server: ServerCertificateItem, protocol: ProtocolStorage.PROTOCOL, certificateItem: CertificateDocument) {
        mServer.fetched(server)
        mProtocol.fetched(protocol)
        mCertificate.certificate(certificateItem)
    }

    fun getFetched() = fetched
    fun getUsable() = mCertificate.certificate()
    fun getProtocol() = mProtocol.fetched()

    fun checkCertificate(
        servID: ServerCertificateItem,
        protocol: ProtocolStorage.PROTOCOL,
        callback: (CHECK) -> Unit
    ) {
        state.let {
            callback(
                when {
                    it == STATE.FAIL ||
                            CertificateStorage.instance.certificate() == null ->
                        CHECK.EMPTY
                    it == STATE.RETRY || it == STATE.GET || it == STATE.GENERATE ->
                        CHECK.WAIT
                    mServer.fetched() == null || mProtocol.fetched() == ProtocolStorage.PROTOCOL.NOTFOUND || mCertificate.certificate() == null ->
                        CHECK.ERROR
                    mServer.fetched() == servID && mProtocol.fetched() == protocol && mCertificate.certificate() != null ->
                        CHECK.USABLE
                    else ->
                        CHECK.DIFFERENT
                }
            )
        }
    }


    fun fetchCertificate(
        server: ServerCertificateItem,
        protocol: ProtocolStorage.PROTOCOL,
        callback: (STATE) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            callback(STATE.GET)
            getCertificateCall(
                servID = server.id.toString(),
                onSuccess = { response ->
                    response.data?.items?.filter { it?.server?.id == server.id }?.firstOrNull{
                        it?.protocols == protocol.protocolText().lowercase()
                    }?.document?.let {
                        fetched = it
                        callback(STATE.FETCHED)
                        save(server, protocol, it)
                    } ?: run {
                        callback(STATE.GENERATE)
                        generateCertificate(server, protocol, callback)
                    }
                },
                onFailure = {
                    callback(STATE.RETRY)
                    fetchCertificate(server, protocol, callback)
                }
            )
        }
    }

    private fun generateCertificate(
        server: ServerCertificateItem,
        protocol: ProtocolStorage.PROTOCOL,
        callback: (STATE) -> Unit
    ) {
        generateCertificateCall(
            server.id.toString(),
            protocol,
            onFailure = {
                callback(STATE.FAIL)
            },
            onSuccess = { response ->
                response.data?.document?.let {
                    callback(STATE.FETCHED)
                    save(server, protocol, it)
                } ?: kotlin.run {
                    callback(STATE.FAIL)
                }
            }
        )
    }

    private fun getCertificateCall(
        servID: String,
        onFailure: (String) -> Unit,
        onSuccess: (CertificateResponse) -> Unit
    ) {
        val payload = CertificatePayload(
            filterserverId = servID,
            sort = ""
        )

        ApiHandler.certificate.getCertificate(
            payload,
            { onFailure(it) },
            { onSuccess(it) }
        )
    }

    private fun generateCertificateCall(
        servID: String,
        protocol: ProtocolStorage.PROTOCOL,
        onFailure: (String) -> Unit,
        onSuccess: (GeneratedCertificateResponse) -> Unit
    ) {
        ApiHandler.certificate.generateCertificate(protocol.key(), servID,
            { onFailure(it) }, { onSuccess(it) }
        )
    }

    private fun ProtocolStorage.PROTOCOL.protocolText(): String {
        val protocolString = this.name.lowercase()
        var name: String
        protocolString.apply {
            split("_").let {
                name = it.first().lowercase().replaceFirstChar { it.uppercase() } + " " + if (it.size > 1) it.last().uppercase() else ""
            }
        }
        return name
    }
}