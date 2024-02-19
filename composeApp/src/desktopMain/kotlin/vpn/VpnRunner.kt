package vpn

import CertificateDocument
import appStorage
import storage.directories.ProtocolStorage
import storage.directories.ProtocolStorage.PROTOCOL
import storage.directories.ProtocolStorage.PROTOCOL.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.StringJoiner

class VpnRunner {

    private lateinit var process: Process
    companion object {
        val instance = VpnRunner()
        private const val root = "sudo src/desktopMain/resources"
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
            OPENVPN_TCP -> "ovpn/ovpncli "
            else -> "wireguard/wg-quick "
        }

    fun start(data: CertificateDocument, callback: (String) -> Unit) {
        val path = makeConfiguration(data)
        execute(path, callback)
    }

    fun kill() {
        if (::process.isInitialized)
            process.destroy()
    }

    private fun makeConfiguration(data: CertificateDocument): String {
        val certificate = assemble(data)

        // Get the directory where the executable is located
        val appDirectory = File(".").absoluteFile.parentFile

        // File name with .txt extension
        val fileName = "temp.conf"

        // Create the file
        val file = File(appDirectory, fileName)

        // Write content to the file
        file.writeText(certificate)

        return file.absoluteFile.absolutePath
    }


    private fun execute(path: String, callback: (String) -> Unit) {
        val command = "${pathBuilder()} $path"

        process = Runtime.getRuntime().exec(
            command
        )

        val reader = BufferedReader(InputStreamReader(process.inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            callback(line.toString())
        }

        val exitCode = process.waitFor()
    }

    private fun assemble(certificate: CertificateDocument): String {
        certificate.apply {
            return StringJoiner("\n")
                .add(config?.common?.joinToString("\n"))
                .add("key-direction ${config?.ca?.keyDirection}")
                .add("<ca>")
                .add("${config?.ca?.cert}</ca>")
                .add("<cert>")
                .add("${client?.cert}</cert>")
                .add("<key>")
                .add("${client?.key}</key>")
                .add("<tls-auth>")
                .add("${config?.tls}</tls-auth>")
                .toString()
                .replace("?", "\n")
        }
    }
}