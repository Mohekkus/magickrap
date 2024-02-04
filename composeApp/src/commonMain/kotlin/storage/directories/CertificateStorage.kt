package storage.directories

import storage.QuickStorage.getString
import storage.QuickStorage.save

class CertificateStorage {

    companion object {
        val instance = CertificateStorage()
        private val protocolStorage = ProtocolStorage()
    }

    enum class DOCUMENT {
        CERTIFICATE;

        private fun key() =name.lowercase()
        fun save(value: String) = value.save(key())
        fun load() = getString(key())
    }

    fun certificate(protocol: String, document: String) {
        DOCUMENT.CERTIFICATE.certificate(protocol, document)
    }

    fun DOCUMENT.certificate(protocol: String, document: String) {
        protocolStorage.protocol(protocol)
        save(document)
    }

    fun certificate() =
        DOCUMENT.CERTIFICATE.load()

    fun document(block: (ProtocolStorage.PROVIDER, String?) -> Unit) {
        block(
            protocolStorage.provider(),
            certificate()
        )
    }
}