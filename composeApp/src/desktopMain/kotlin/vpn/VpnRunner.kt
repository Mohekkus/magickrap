package vpn

import CertificateDocument
import appStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import storage.directories.ProtocolStorage.PROTOCOL.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.StringJoiner

class VpnRunner {

    private lateinit var process: Process
    companion object {
        val instance = VpnRunner()
        private const val PATH = "sudo /Users/mohekkus/development/auxonode-desktop/composeApp/src/desktopMain/resources"
    }

    private fun pathBuilder() = StringJoiner("/")
        .add(PATH).add(byOS()).add(provider())

    private fun byOS() =
        when (System.getProperty("os.name").lowercase()) {
            "windows" -> "windows"
            else -> "darwin"
        }

    private fun provider() =
        when (appStorage.protocol()) {
            OPENVPN_UDP,
            OPENVPN_TCP -> "ovpn/ovpncli"
            else -> "wireguard/wg-quick/darwin.bash up"
        }

    private fun option() =
        when (appStorage.protocol()) {
            OPENVPN_UDP,
            OPENVPN_TCP -> "--compress asym"
            else -> ""
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
        val certificate =
            if (appStorage.protocol() == WIREGUARD) assembleWireguard(data) else assembleOpenVPN(data)

        // Get the directory where the executable is located
        val appDirectory = File(".").absoluteFile.parentFile

        // File name with .txt extension
        val fileName = "temp.${
            if (appStorage.protocol() == WIREGUARD) "conf"
            else "ovpn"
        }"

        // Create the file
        val file = File(appDirectory, fileName)

        // Write content to the file
        file.writeText(certificate)

        return file.absoluteFile.absolutePath
    }


    private fun execute(path: String, callback: (String) -> Unit) {
        val password = "RakhasaMohekkus"
        val command = "sudo -S ${pathBuilder()} $path ${option()}"
        println(command)


        val job = CoroutineScope(Dispatchers.IO).launch {
            process = Runtime.getRuntime().exec(command)

            val outputStream = process.outputStream
            outputStream.write("$password\n".toByteArray())
            outputStream.flush()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
        }

        // Optional: Handle the completion of the job
        job.invokeOnCompletion {
            it?.let {
                println("Job failed: $it")
            } ?: run {
                println("Job completed successfully.")
            }
        }

//        val exitCode = process.waitFor()

//        println("Process exited with code: $exitCode")
    }

    private fun assembleOpenVPN(certificate: CertificateDocument): String {
        certificate.apply {
            return StringJoiner("\n")
                .add(config?.common?.joinToString("\n"))
                .add("compress lz4\n" +
                        "comp-lzo no")
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

    private fun assembleWireguard(certificate: CertificateDocument): String {
        certificate.apply {
            return StringJoiner("\n")
                .apply {
                    add("[Interface]")
                    client?.apply {
                        add("Address = $address")
                        add("PrivateKey = $privateKey")
                        add("Port = $port")
                    }

                    add("[Peer]")
                    config?.apply {
                        add("AllowedIPs = $allowedIp")
                        add("PublicKey = $publicKey")
                        add("PresharedKey = $presharedKey")
                        add("Endpoint = $endpoint")
                    }
                }
                .toString()
        }
    }
}