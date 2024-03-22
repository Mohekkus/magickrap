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
        STATUS,
        SERVER,
        PROTOCOL,
        CERTIFICATE;

        private fun key() = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load() = getString(key())
    }

}