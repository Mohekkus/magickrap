package vpn.essential

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandRunner {

    private lateinit var process: Process
    companion object {
        val instance = CommandRunner()
    }

    fun run(
        command: String,
        onDebugLog: (String) -> Unit,
        onErrorLog: (String) -> Unit,
        onFinished: (String?) -> Unit
    ) {
        val job = CoroutineScope(Dispatchers.IO).launch {
            process = ProcessBuilder(command).start()
                .apply {
                    val inputReader = BufferedReader(
                        InputStreamReader(inputStream)
                    )
                    val errorReader = BufferedReader(
                        InputStreamReader(errorStream)
                    )

                    while (process.isAlive) {
                        onDebugLog(inputReader.readLine())
                        onErrorLog(errorReader.readLine())
                    }
                }
        }

        job.invokeOnCompletion {
            onFinished(it?.message)
        }
    }

    fun status(): Boolean =
        if (::process.isInitialized)
            if (process.isAlive)
                true
            else false
        else false
}