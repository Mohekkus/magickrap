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

    fun getString(keys: String) = storage.getStringOrNull(keys)
    fun getInt(keys: String) = storage.getIntOrNull(keys)
    fun getBoolean(keys: String) = storage.getBooleanOrNull(keys)
    fun getLong(keys: String) = storage.getLongOrNull(keys)
    fun getDouble(keys: String) = storage.getDoubleOrNull(keys)
    fun getFloat(keys: String) = storage.getFloatOrNull(keys)
}