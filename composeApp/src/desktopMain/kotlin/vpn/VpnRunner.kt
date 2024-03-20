package vpn

import CertificateDocument
import appStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import storage.directories.ProtocolStorage
import storage.directories.ProtocolStorage.PROTOCOL.*
import vpn.essential.ExtractingSources
import vpn.essential.ExtractingSources.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class VpnRunner: Bulletin {

    private lateinit var process: Process

    // Get the directory where the executable is located
    private val appDirectory = when {
        System.getProperty("os.name").startsWith("Windows") -> System.getenv("APPDATA")
        System.getProperty("os.name").startsWith("Mac") -> "${System.getProperty("user.home")}/Library/Application Support/Auxonode - ${LocalDate.now()}/"
        else -> throw UnsupportedOperationException("Unsupported operating system")
    }

    companion object {
        val instance = VpnRunner()
    }

    private var binariesCopier: ExtractingSources = ExtractingSources(this)
    private var isPrepared = binariesCopier.isAvailable()


    fun start(data: CertificateDocument, callback: (String) -> Unit) {
        if (!isPrepared) {
            binariesCopier.isAvailable()
            callback(
                "Please wait, our system still preparing Application full functionality"
            )
            return
        }

        val config = makeConfiguration(data)
        executeMacOs(config, callback)
    }

    fun status() =
        if (::process.isInitialized)
            if (process.isAlive) true else false
        else false

    private fun makeConfiguration(data: CertificateDocument): String {
        val certificate =
            if (appStorage.protocol() == WIREGUARD) assembleWireguard(data)
            else assembleOpenVPN(data)

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

    private fun executeMacOs(path: String, callback: (String) -> Unit) {
        val job = CoroutineScope(Dispatchers.IO).launch {
            process =
                when (appStorage.protocol()) {
                    ProtocolStorage.PROTOCOL.OPENVPN_UDP,
                    ProtocolStorage.PROTOCOL.OPENVPN_TCP -> {
                        File(VpnConstant.getOvpn).let {
                            if (!it.canExecute())
                                it.setExecutable(true)
                        }

                        ProcessBuilder("sudo", VpnConstant.getOvpn, path, "-c", "asym").start()
                    }
                    else -> {
                        File(VpnConstant.getBash).let {
                            if (!it.canExecute())
                                it.setExecutable(true)
                        }

                        File(VpnConstant.getWg).let {
                            if (!it.canExecute())
                                it.setExecutable(true)
                        }

                        File(VpnConstant.getGo).let {
                            if (!it.canExecute())
                                it.setExecutable(true)
                        }

                        ProcessBuilder("sudo", VpnConstant.getBash, VpnConstant.getWireguard, "up", path)
                            .apply {
                                println(this.command().toString().replace(",", ""))
                            }
                            .start()
                    }
                }

            val inputReader = process.inputReader()
            val errorReader = process.errorReader()
            var errorLine = ""
            var inputLine = ""

            while (
                inputReader.readLine()?.also { inputLine = it } != null ||
                errorReader.readLine()?.also { errorLine = it } != null
            ) {
                if (
                    errorLine.contains(
                        "sudo: a terminal is required to read the password; either use the -S option to read from standard input or configure an askpass helper") ||
                    errorLine.contains("sudo: a password is required")
                    )
                    executeOsa {
                        if (it.isEmpty())
                            executeMacOs(path, callback)
                        else
                            callback("Failed")
                    }

                println(errorLine)
                println(inputLine)

                when (appStorage.protocol()) {
                    ProtocolStorage.PROTOCOL.OPENVPN_UDP,
                    ProtocolStorage.PROTOCOL.OPENVPN_TCP -> callback(inputLine.replace("EVENT:", ""))
                    else -> {}
                }

                errorLine = ""

                when (appStorage.protocol()) {
                    ProtocolStorage.PROTOCOL.OPENVPN_UDP,
                    ProtocolStorage.PROTOCOL.OPENVPN_TCP ->
                        if (!process.isAlive)
                            callback("DISCONNECTED")
                    else -> {}
                }
            }

            process.waitFor(30, TimeUnit.SECONDS)
        }

        job.invokeOnCompletion {
            it?.let {
                println(it.message)
            } ?: run {
                println("finished")
                if (appStorage.protocol() == WIREGUARD)
                    checkWireguard(callback)
            }
        }
    }

    private fun checkWireguard(callback: (String) -> Unit) {
        ProcessBuilder("sudo", VpnConstant.getWg)
            .apply {

            }
            .start()
            .apply {
                if (inputReader().readLine()?.isEmpty() == true)
                    callback("DISCONNECTED")
                else
                    callback("CONNECTED")
            }
    }

    private fun sudoersPath() = StringJoiner(", ")
        .add(
            VpnConstant.getOvpn.replace(" ", "\\\\ ")
        )
        .add(
            VpnConstant.getBash.replace(" ", "\\\\ ")
        )
        .add(
            VpnConstant.getWg.replace(" ", "\\\\ ")
        )
        .add(
            VpnConstant.getWireguard.replace(" ", "\\\\ ")
        )
        .add("/bin/kill")

    private fun executeOsa(callback: (String) -> Unit) {
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
        val osaProcess = ProcessBuilder("osascript", "-e", scriptCommand).start()

        // Read the output of the command
        val errReader = BufferedReader(InputStreamReader(osaProcess.errorStream))
        val inReader = BufferedReader(InputStreamReader(osaProcess.inputStream))
        while (osaProcess.isAlive) {
            errReader.readLine()?.let {
                println(it)
            }
            inReader.readLine()?.let {
                println(it)
            }
        }

        // Wait for the process to complete
        val exitCode = osaProcess.waitFor()

        if (exitCode == 0) {
            println("Sudoers file modified successfully.")
//            callback("")
        } else {
            println("Failed to modify sudoers file.")
//            callback("Failed dunno why")
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
//            println(Gson().toJson(certificate))

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
                        add("ListenPort = $port")
                        add("DNS = $dns")
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

    override fun onPreparationComplete() {
        isPrepared = true
    }

    override fun onPreparationFailed(message: String) {
        println(message)
    }
}