package etc

import androidx.compose.ui.text.intl.Locale
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.net.NetworkInterface
import java.util.*

class DeviceInfo {

    companion object {
        val instance = DeviceInfo()
    }

    val country = Locale.current.region
    val publicIP = runBlocking { getPublicIp() }
    val macAddress: String? = deviceMacAddress()

    val devId = UUID.randomUUID().toString()
    val devModel = "Desktop"

    val osName = getProperty("os.name")
    val osArch = getProperty("os.arch")
    val osVers = getProperty("os.version")
    val os = "$osName $osVers $osArch"

    private suspend fun getPublicIp() = CoroutineScope(Dispatchers.IO).async {
        val client = HttpClient(Java)
        val response = client.get(Global.URL_PUBLIC_IP)
        client.close()
        return@async response.bodyAsText()
    }.await()

    private fun getProperty(identifier: String) = System.getProperty(identifier)
    private fun deviceMacAddress(): String? {
        return try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val hardwareAddress = networkInterface.hardwareAddress
                if (hardwareAddress != null && hardwareAddress.isNotEmpty()) {
                    val macAddressStringBuilder = StringBuilder()
                    for (byte in hardwareAddress) {
                        macAddressStringBuilder.append(String.format("%02X:", byte))
                    }
                    macAddressStringBuilder.deleteCharAt(macAddressStringBuilder.length - 1)
                    return macAddressStringBuilder.toString()
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}