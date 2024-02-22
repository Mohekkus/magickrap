package storage.directories

import storage.QuickStorage.getString
import storage.QuickStorage.save

class ProtocolStorage {

    companion object {
        val instance = ProtocolStorage()
    }

    enum class OPTION {
        PROTOCOL;
        private fun key() = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load() = getString(key())
    }

    enum class PROTOCOL {
        OPENVPN_UDP,
        OPENVPN_TCP,
        WIREGUARD;

        fun key() = name.lowercase()
    }

    enum class PROVIDER {
        OPENVPN,
        WIREGUARD
    }

    fun protocol(value: String) {
        OPTION.PROTOCOL.save(value)
    }

    fun parameter() = OPTION.PROTOCOL.load() ?: "automatic"

    fun protocol(): PROTOCOL {
        println("PROTOCOL STORAGE ${parameter()}")

        println("PROTOCOL STORAGE IS WIREGUARD ${parameter() == PROTOCOL.WIREGUARD.key()}")
        println("PROTOCOL STORAGE IS UDP OPENVPN ${parameter() == PROTOCOL.OPENVPN_TCP.key()}")
        println("PROTOCOL STORAGE IS TCP OPENVPN ${parameter() == PROTOCOL.OPENVPN_UDP.key()}")

        return when (parameter()) {
            PROTOCOL.OPENVPN_UDP.key() -> PROTOCOL.OPENVPN_UDP
            PROTOCOL.OPENVPN_TCP.key() -> PROTOCOL.OPENVPN_TCP
            else -> PROTOCOL.WIREGUARD
        }
    }

    fun provider(): PROVIDER {
        return when (parameter()) {
            PROTOCOL.OPENVPN_UDP.key(), PROTOCOL.OPENVPN_TCP.key() -> PROVIDER.OPENVPN
            else -> PROVIDER.WIREGUARD
        }
    }

}