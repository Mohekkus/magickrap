package vpn.essential

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vpn.VpnConstant
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Executors

class ExtractingSources(
    private val _interface: Bulletin
) {

    companion object {
        private val source = File(System.getProperty("compose.application.resources.dir"))
            .resolve("${VpnConstant.byOS}/")
        private val destination = VpnConstant.appDirectory.apply {
            File(this).apply {
                if (!exists())
                    mkdirs()
            }
        }
    }

    interface Bulletin {
        fun onPreparationComplete()
        fun onPreparationFailed(message: String)
    }

    fun isAvailable(): Boolean {
        return File(destination).resolve(VpnConstant.provider).apply {
            if (!exists())
                source.copyRecursively(File(destination), true)

            if (!canExecute())
                setExecutable(true)
        }.exists()
    }
}