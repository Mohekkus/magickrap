package etc

import NativeDeviceInfo
import androidx.compose.ui.text.intl.Locale
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import java.util.*


object DeviceInfo {

    val country = Locale.current.region

    val devId = UUID.randomUUID().toString()
    val devModel = NativeDeviceInfo.getHardwareInfo()

    fun getProperty(identifier: String) = System.getProperty(identifier)
    val osName = getProperty("os.name")
    val osArch = getProperty("os.arch")
    val osVers = getProperty("os.version")
    val os = "$osName $osVers $osArch"


    val publicIP = runBlocking { getPublicIp() }
    suspend fun getPublicIp() = CoroutineScope(Dispatchers.IO).async {
        val client = HttpClient(Java)
        val response = client.get(Global.URL_PUBLIC_IP)
        client.close()
        return@async response.bodyAsText()
    }.await()

}