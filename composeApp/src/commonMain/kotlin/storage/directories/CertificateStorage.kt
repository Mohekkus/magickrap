package storage.directories

import storage.QuickStorage.getString
import storage.QuickStorage.save

class CertificateStorage {

    companion object {
        val instance = CertificateStorage()
    }

    enum class DOCUMENT {
        CERTIFICATE;

        private fun key() =name.lowercase()
        fun save(value: String) = value.save(key())
        fun load() = getString(key())
    }

    fun certificate(document: String) {
        DOCUMENT.CERTIFICATE.save(document)
    }

    fun certificate() =
        DOCUMENT.CERTIFICATE.load()
}