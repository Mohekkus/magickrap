package storage.directories

import http.certificate.model.response.ServerCertificateResponse
import storage.QuickStorage
import storage.QuickStorage.save

class ServerStorage {

    companion object {
        val instance = ServerStorage()
    }

    enum class CLASSIFICATION {
        LIST;

        private fun key(): String = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load(): String? = QuickStorage.getString(key())
    }

    fun servers(value: String) {
        CLASSIFICATION.LIST.save(value)
    }
    fun servers() =
        CLASSIFICATION.LIST.load()

}