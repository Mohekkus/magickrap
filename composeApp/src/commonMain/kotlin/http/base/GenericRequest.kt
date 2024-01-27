package http.base

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object GenericRequest {
    fun runner(requests: suspend () -> HttpResponse, onComplete: (HttpResponse) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(
                requests()
            )
        }
    }

    suspend fun post(appendedPath: List<String>, body: Any?): HttpResponse {
        return KtorClient.instance.post {
            url.appendEncodedPathSegments(appendedPath)
            setBody(body)
        }
    }
}