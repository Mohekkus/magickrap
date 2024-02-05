package storage

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import http.certificate.model.response.ServerCertificateResponse
import storage.directories.CertificateStorage
import storage.directories.ProtocolStorage
import storage.directories.ServerStorage
import storage.directories.UserStorage

class Storage {

    companion object {
        val instance = Storage()
    }

    private val user = UserStorage.instance
    private val certificate = CertificateStorage.instance
    private val protocol = ProtocolStorage.instance
    private val server = ServerStorage.instance

    // USER AUTHENTICATION
    fun logged(token: String) = user.token(token)
    fun logged(token: String, deviceId: String) = user.logged(token, deviceId)
    fun deviceId(deviceId: String) = user.device(deviceId)
    fun token() = user.token()
    fun deviceId() = user.device()

    // PROTOCOL
    fun protocol(value: String) = protocol.protocol(value)
    fun protocol() = protocol.protocol()
    fun parameter() = protocol.parameter()

    // DOCUMENT
    fun document(document: String) = certificate.certificate(document)
    fun document(block: (ProtocolStorage.PROVIDER, String?) -> Unit) =
        block(
            protocol.provider(),
            certificate.certificate()
        )

    // SERVERS
    fun servers(value: List<ServerCertificateResponse.ServerCertificateData.ServerCertificateItem?>) {
        server.servers(
            Gson().toJson(value)
        )
    }
    fun servers() {
        val serverList = server.servers()
        if (serverList?.isEmpty() == true)
            return

        val type = object : TypeToken<
                List<
                        ServerCertificateResponse
                            .ServerCertificateData.ServerCertificateItem
                        >
                >() {}
        Gson().fromJson(
            serverList,
            type
        )
    }
}