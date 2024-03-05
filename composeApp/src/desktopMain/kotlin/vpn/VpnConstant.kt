package vpn

import appStorage
import storage.directories.ProtocolStorage
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate
import java.util.StringJoiner

object VpnConstant {
    private val PATH_APP = "Auxonode - ${LocalDate.now()}/Binaries"
    private const val PATH_OVPN = "ovpn/ovpncli"
    private const val PATH_WIREGUARD = "wireguard/wg-quick/darwin.bash"
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
}