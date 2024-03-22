package storage.directories

import storage.QuickStorage.getString
import storage.QuickStorage.save

class ProtocolStorage {

    companion object {
        val instance = ProtocolStorage()
    }

    enum class OPTION {
        FETCHED,
        PROTOCOL;

        private fun key() = name.lowercase()
        fun save(value: String) = value.save(key())
        fun load() = getString(key())
    }

    enum class PROTOCOL {
        OPENVPN_UDP,
        OPENVPN_TCP,
        WIREGUARD,
        NOTFOUND;

        fun key() = name.lowercase()
    }

    enum class PROVIDER {
        OPENVPN,
        WIREGUARD
    }

    fun protocol(value: PROTOCOL) {
        OPTION.PROTOCOL.save(value.key())
    }

    fun parameter() = OPTION.PROTOCOL.load() ?: "automatic"

    fun protocol(): PROTOCOL = when (parameter()) {
            PROTOCOL.OPENVPN_UDP.key() -> PROTOCOL.OPENVPN_UDP
            PROTOCOL.OPENVPN_TCP.key() -> PROTOCOL.OPENVPN_TCP
            else -> PROTOCOL.WIREGUARD
        }


    fun provider(): PROVIDER = when (parameter()) {
            PROTOCOL.OPENVPN_UDP.key(), PROTOCOL.OPENVPN_TCP.key() -> PROVIDER.OPENVPN
            else -> PROVIDER.WIREGUARD
        }

    fun fetched(protocol: PROTOCOL) {
        OPTION.FETCHED.save(protocol.key())
    }

    fun fetched() = when (OPTION.FETCHED.load()) {
        PROTOCOL.OPENVPN_UDP.key() -> PROTOCOL.OPENVPN_UDP
        PROTOCOL.OPENVPN_TCP.key() -> PROTOCOL.OPENVPN_TCP
        PROTOCOL.WIREGUARD.key() -> PROTOCOL.WIREGUARD
        else -> PROTOCOL.NOTFOUND
    }
}