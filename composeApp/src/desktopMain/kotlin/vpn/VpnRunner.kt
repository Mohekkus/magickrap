package vpn

import CertificateDocument
import appStorage
import storage.directories.ProtocolStorage
import storage.directories.ProtocolStorage.PROTOCOL
import storage.directories.ProtocolStorage.PROTOCOL.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringJoiner

class VpnRunner {

    companion object {
        val instance = VpnRunner()
        private const val root = "sudo src/desktopMain/resources"
        private lateinit var process: Process
    }

    private fun pathBuilder() = StringJoiner("/")
        .add(root).add(byOS()).add(provider())

    private fun byOS() =
        when (System.getProperty("os.name").lowercase()) {
            "windows" -> "windows"
            else -> "darwin"
        }

    private fun provider() =
        when (appStorage.protocol()) {
            OPENVPN_UDP,
            OPENVPN_TCP -> "ovpn/ovpncli"
            else -> "wireguard/wg-quick"
        }


    fun execute(config: CertificateDocument? = null, callback: (String) -> Unit) {
        process = Runtime.getRuntime().exec(pathBuilder().toString())
        val reader = BufferedReader(InputStreamReader(process.inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            callback(line.toString())
        }

        val exitCode = process.waitFor()
    }

    fun getStatus() {

    }
}