package storage.directories

import storage.QuickStorage
import storage.QuickStorage.save

class UserStorage {

    companion object {
        val instance = UserStorage()
    }

    enum class CLASSIFICATION {
        TOKEN,
        `DEVICE-ID`;

        private fun key(): String = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load(): String? = QuickStorage.getString(key())
    }

    fun token(value: String) = CLASSIFICATION.TOKEN.save(value)
    fun token(): String? = CLASSIFICATION.TOKEN.load()

    fun device(value: String) = CLASSIFICATION.`DEVICE-ID`.save(value)
    fun device() = CLASSIFICATION.`DEVICE-ID`.load()

    fun logged(token: String, deviceId: String) {
        token(token)
        device(deviceId)
    }
}