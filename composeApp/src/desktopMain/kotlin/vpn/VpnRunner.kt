package vpn

import CertificateDocument
import appStorage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import storage.directories.ProtocolStorage.PROTOCOL.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.StringJoiner

class VpnRunner {

    private lateinit var process: Process
    private lateinit var path: String
    companion object {
        val instance = VpnRunner()
        private const val PATH = "/Users/mohekkus/development/auxonode-desktop/composeApp/src/desktopMain/resources"
        private const val PATH_OVPN = "ovpn/ovpncli"
        private const val PATH_WIREGUARD = "wireguard/wg-quick/darwin.bash"
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
            OPENVPN_TCP -> PATH_OVPN
            else -> "$PATH_WIREGUARD up"
        }

    private fun option() =
        when (appStorage.protocol()) {
            OPENVPN_UDP,
            OPENVPN_TCP -> "--compress asym"
            else -> ""
        }

    fun start(data: CertificateDocument, callback: (String) -> Unit) {
        if (::process.isInitialized)
            if (process.isAlive) {
                terminate()
                return
            }

        val path = makeConfiguration(data)
        execute(path, callback)
    }

    fun status() =
        if (::process.isInitialized)
            if (process.isAlive) true else false
        else false

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
        val command = "sudo ${pathBuilder()} $path ${option()}"
        println(command)

        val job = CoroutineScope(Dispatchers.IO).launch {
            process = Runtime.getRuntime().exec(command)

            val ireader = BufferedReader(InputStreamReader(process.inputStream))
            val ereader = BufferedReader(InputStreamReader(process.errorStream))
            var iline: String? = ""
            var eline: String? = ""
            while (process.isAlive) {
                ireader.readLine()?.also {
                    iline = it
                }
                println(iline)
                if (iline?.contains("CONNECTING") == true)
                    callback(iline.toString())

                ereader.readLine()?.also {
                    eline = it
                }
                println(eline)
                if (eline?.contains("sudo: a terminal is required to read the password; either use the -S option to read from standard input or configure an askpass helper") == true)
                    executeOsa {
                        if (it)
                            execute(path, callback)
                        else
                            callback("Failed")
                    }
                else eline?.let { callback(it) }

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
    }

    private fun sudoersPath() = StringJoiner(", ")
        .add(
            StringJoiner("/")
                .add(PATH)
                .add(byOS())
                .add(PATH_OVPN)
                .toString()
        )
        .add(
            StringJoiner("/")
                .add(PATH)
                .add(byOS())
                .add(PATH_WIREGUARD)
                .toString()
        )
        .add("/bin/kill")

    private fun executeOsa(callback: (Boolean) -> Unit) {
        // Execute the whoami command to get the current user
        val processBuilderWhoami = ProcessBuilder("whoami")
        val processWhoami = processBuilderWhoami.start()
        val readerWhoami = BufferedReader(InputStreamReader(processWhoami.inputStream))
        val username = readerWhoami.readLine().trim()

        // Replace "/path/to/command" with the actual path to your command
        val command = sudoersPath()

        // Build the AppleScript command for password prompt and command execution
        val scriptCommand = "do shell script \"echo \'$username ALL=(ALL) NOPASSWD: $command\' | sudo tee -a /etc/sudoers.d/custom_sudoers\" with administrator privileges"

        // Execute the AppleScript command using ProcessBuilder
        val processBuilder = ProcessBuilder("osascript", "-e", scriptCommand)
        val process = processBuilder.start()

        // Read the output of the command
        val reader = BufferedReader(InputStreamReader(process.errorStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }

        // Wait for the process to complete
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            println("Sudoers file modified successfully.")
            callback(true)
        } else {
            println("Failed to modify sudoers file.")
            callback(false)
        }
    }

    fun terminate() {
        val grepprocess = Runtime.getRuntime().exec("pgrep ovpncli")
        val reader = BufferedReader(InputStreamReader(grepprocess.inputStream))
        val pid = reader.readLine()?.toIntOrNull()
        reader.close()

        if (pid != null) {
            // Terminate the process using its PID
            Runtime.getRuntime().exec("sudo kill $pid")
//            ProcessBuilder("sudo", "kill", pid.toString()).start().waitFor()
//            process.destroy()
            println("OpenVPN process with PID $pid terminated.")
        } else {
            println("OpenVPN process not found.")
        }
    }

    private fun assembleOpenVPN(certificate: CertificateDocument): String {
        certificate.apply {
            println(Gson().toJson(certificate))

            return StringJoiner("\n")
                .add(config?.common?.joinToString("\n"))
                .add("compress lz4\n" +
                        "comp-lzo no")
                .add("key-direction ${config?.ca?.keyDirection}")

                .add("<ca>")
                .add("${config?.ca?.cert}")
                .add("</ca>")

                .add("<cert>")
                .add("${client?.cert}")
                .add("</cert>")

                .add("<key>")
                .add("${client?.key}")
                .add("</key>")

                .add("<tls-auth>")
                .add("${config?.tls}")
                .add("</tls-auth>")

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