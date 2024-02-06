package storage.directories

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import etc.Global.toClass
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.ServerCertificateItem
import storage.QuickStorage
import storage.QuickStorage.save

class ServerStorage {

    companion object {
        val instance = ServerStorage()
    }

    enum class CLASSIFICATION {
        LIST,
        FAVORITE;

        private fun key(): String = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load(): String? = QuickStorage.getString(key())
    }

    fun servers(value: String) {
        CLASSIFICATION.LIST.save(value)
    }
    fun servers() =
        CLASSIFICATION.LIST.load()
    fun favorite(): MutableList<ServerCertificateItem>? {
        val favoriteToken = object : TypeToken<
                MutableList<ServerCertificateItem>>() {}

        println(CLASSIFICATION.FAVORITE.load())

        return CLASSIFICATION.FAVORITE.load().toClass(favoriteToken)
    }
    fun favorite(value: ServerCertificateItem) {
        var data = mutableListOf(value)
        if (favorite()?.isEmpty() == false) {
            data = favorite()!!.apply {
                add(value)
            }
        }

        println(data)

        CLASSIFICATION.FAVORITE.save(
            Gson().toJson(data)
        )
    }
    fun unfavorite(value: ServerCertificateItem) {
        favorite()?.apply {
            val data = this
            if (isEmpty()) return

            find { value.id == it.id }.let {
                data.remove(it)
            }

            CLASSIFICATION.FAVORITE.save(
                Gson().toJson(data)
            )
        }
    }
    fun purge() = CLASSIFICATION.FAVORITE.save("")
}