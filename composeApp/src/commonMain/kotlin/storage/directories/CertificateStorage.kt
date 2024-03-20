package storage.directories

import CertificateDocument
import com.google.gson.Gson
import storage.QuickStorage.getString
import storage.QuickStorage.save

class CertificateStorage {

    companion object {
        val instance = CertificateStorage()
        val certificate = instance.certificate()
    }

    enum class DOCUMENT {
        CERTIFICATE;

        private fun key() = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load() = getString(key())
    }

    fun certificate(document: String) {
        DOCUMENT.CERTIFICATE.save(document)
    }

    fun certificate(): CertificateDocument? {
        return Gson().fromJson(DOCUMENT.CERTIFICATE.load(), CertificateDocument::class.java)
    }
//        DOCUMENT.CERTIFICATE.load()
}