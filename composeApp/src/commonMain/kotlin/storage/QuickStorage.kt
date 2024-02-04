package storage

import com.russhwolf.settings.Settings

object QuickStorage {
    private val storage = Settings()
    fun has(keys: String) = storage.hasKey(keys)

    fun String.save(keys: String) = storage.putString(keys, this)
    fun Int.save(keys: String) = storage.putInt(keys, this)
    fun Boolean.save(keys: String) = storage.putBoolean(keys, this)
    fun Long.save(keys: String) = storage.putLong(keys, this)
    fun Double.save(keys: String) = storage.putDouble(keys, this)
    fun Float.save(keys: String) = storage.putFloat(keys, this)

    fun String.load(callback: (String?) -> Unit) = callback(storage.getStringOrNull(this))
    fun String.load(callback: (Int?) -> Unit) = callback(storage.getIntOrNull(this))
    fun String.load(callback: (Boolean?) -> Unit) = callback(storage.getBooleanOrNull(this))
    fun String.load(callback: (Long?) -> Unit) = callback(storage.getLongOrNull(this))
    fun String.load(callback: (Double?) -> Unit) = callback(storage.getDoubleOrNull(this))
    fun String.load(callback: (Float?) -> Unit) = callback(storage.getFloatOrNull(this))
}