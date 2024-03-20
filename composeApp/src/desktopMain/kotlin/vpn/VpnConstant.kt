package vpn

import appStorage
import storage.directories.ProtocolStorage
import java.nio.file.Paths
import java.time.LocalDate
import java.util.StringJoiner

object VpnConstant {
    private val PATH_APP = "Auxonode - ${LocalDate.now()}/Binaries"
    private const val PATH_OVPN = "ovpn/ovpncli"
    private const val PATH_WIREGUARD = "wireguard/wg-quick/darwin.bash"
    private const val PATH_BASH = "wireguard/bash"
    private const val PATH_WG = "wireguard/wg-quick/wg"
    private const val PATH_GO = "wireguard/wg-quick/wireguard-go"

    val appDirectory =
        when {
            System.getProperty("os.name").startsWith("Windows") -> "${System.getenv("APPDATA")}/$PATH_APP"
            System.getProperty("os.name").startsWith("Mac") -> "${System.getProperty("user.home")}/Library/Application Support/$PATH_APP"
            else -> throw UnsupportedOperationException("Unsupported operating system")
        }

    val byOS =
        when (System.getProperty("os.name").lowercase()) {
            "windows" -> "windows"
            else -> "darwin"
        }

    val provider =
        when (appStorage.protocol()) {
            ProtocolStorage.PROTOCOL.OPENVPN_UDP,
            ProtocolStorage.PROTOCOL.OPENVPN_TCP -> PATH_OVPN
            else -> PATH_WIREGUARD
        }

    val option =
        when (appStorage.protocol()) {
            ProtocolStorage.PROTOCOL.OPENVPN_UDP,
            ProtocolStorage.PROTOCOL.OPENVPN_TCP -> "--compress asym"
            else -> ""
        }


    val getOvpn = Paths.get(
        StringJoiner("/")
            .add(appDirectory)
            .add(PATH_OVPN)
            .toString()
    )
        .toAbsolutePath()
        .toString()

    val getBash =
        Paths.get(
            StringJoiner("/")
                .add(appDirectory)
                .add(PATH_BASH)
                .toString()
        )
            .toAbsolutePath()
            .toString()

    val getWg =
        Paths.get(
            StringJoiner("/")
                .add(appDirectory)
                .add(PATH_WG)
                .toString()
        )
            .toAbsolutePath()
            .toString()


    val getGo =
        Paths.get(
            StringJoiner("/")
                .add(appDirectory)
                .add(PATH_GO)
                .toString()
        )
            .toAbsolutePath()
            .toString()

    val getWireguard =
        Paths.get(
            StringJoiner("/")
                .add(appDirectory)
                .add(PATH_WIREGUARD)
                .toString()
        )
            .toAbsolutePath()
            .toString()

    val byProtocol =
        when (appStorage.protocol()) {
            ProtocolStorage.PROTOCOL.OPENVPN_UDP,
            ProtocolStorage.PROTOCOL.OPENVPN_TCP -> getOvpn
            else -> getWireguard
        }
}