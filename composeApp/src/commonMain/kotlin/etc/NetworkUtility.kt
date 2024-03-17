package etc

import http.base.AdditionalClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object NetworkUtility {

    fun getIpAddress(callback: (String?) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                AdditionalClient().client("checkip.amazonaws.com").get {}
                    .apply {
                        callback(
                            bodyAsText()
                        )
                    }
            } catch (e: Exception) {
                callback(e.message)
//            getIpAddress(callback)
            }
        }
    }
}