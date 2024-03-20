package storage.directories

import androidx.compose.runtime.key
import storage.QuickStorage.getString
import storage.QuickStorage.save

class ConnectedStorage {

    companion object {
        val instance = ConnectedStorage()
    }

    data class CONFIGURATION(
        val server: String?,
        val protocol: String?,
        val certificate: String?
    )

    enum class CONNECTED {
        USAGE,
        SERVER,
        CERTIFICATE,
        PROTOCOL;

        private fun key() = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load() = getString(key())
    }

    fun save(server: String, protocol: String, certificate: String) {
        CONNECTED.SERVER.save(server)
        CONNECTED.PROTOCOL.save(protocol)
        CONNECTED.CERTIFICATE.save(certificate)
        CONNECTED.USAGE.save(
            System.currentTimeMillis().toString()
        )
    }
    fun load() =
        CONFIGURATION(
            CONNECTED.SERVER.load(),
            CONNECTED.PROTOCOL.load(),
            CONNECTED.CERTIFICATE.load()
        )

}