package storage

import CertificateDocument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import http.certificate.model.response.ServerCertificateResponse
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.ServerCertificateItem
import storage.directories.*

class Storage {

    companion object {
        val instance = Storage()
    }

    private val user = UserStorage.instance
    private val certificate = CertificateStorage.instance
    private val protocol = ProtocolStorage.instance
    private val server = ServerStorage.instance
    private val connection = ConnectedStorage.instance

    // VPN CONNECTED
    fun setConnectedConfiguration(server: String, protocol: String, certificate: String) {
        connection.save(server, protocol, certificate)
    }
    fun getConnectedConfiguration() = connection.load()

    // USER AUTHENTICATION
    fun logged(token: String) = user.token(token)
    fun logged(token: String, deviceId: String) = user.logged(token, deviceId)
    fun deviceId(deviceId: String) = user.device(deviceId)
    fun token() = user.token()
    fun deviceId() = user.device()

    // PROTOCOL
    fun protocol(value: ProtocolStorage.PROTOCOL) = protocol.protocol(value.key())
    fun protocol(value: String) = protocol.protocol(value)
    fun protocol() = protocol.protocol()
    fun parameter() = protocol.parameter()

    // DOCUMENT
    fun document() = certificate.certificate()
    fun document(document: CertificateDocument?) {
        certificate.certificate(
            Gson().toJson(document)
        )
    }
    fun document(block: (ProtocolStorage.PROVIDER, CertificateDocument?) -> Unit) =
        block(
            protocol.provider(),
            certificate.certificate()
        )

    // SERVERS
    fun servers(value: List<ServerCertificateItem?>) {
        server.servers(
            Gson().toJson(value)
        )
    }
    fun servers(): List<ServerCertificateItem>? {
        val serverList = server.servers()
        if (serverList?.isEmpty() == true)
            return null

        val type = object : TypeToken<
                List<
                        ServerCertificateItem
                        >
                >() {}
        return Gson().fromJson(
            serverList,
            type
        )
    }

    fun favorite(value: ServerCertificateItem) {
        server.favorite(value)
    }
    fun favorite() = server.favorite()
    fun unfavorited(value: ServerCertificateItem) = server.unfavorite(value)

//    fun purge() = server.purge()

    fun save(value: ServerCertificateItem) = server.save(value)
    fun saved() = server.saved()
}