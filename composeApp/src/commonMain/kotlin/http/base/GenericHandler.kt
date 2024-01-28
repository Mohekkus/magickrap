package http.base

import http.base.response.ResponseStatus
import http.base.response.ResponseWrapper
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeoutException

object GenericHandler {
    fun runner(requests: suspend () -> HttpResponse, callback: (ResponseWrapper<*>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                requests().apply {
                    callback(
                        when (status) {
                            HttpStatusCode.OK -> ResponseWrapper.success(bodyAsText())
                            else -> ResponseWrapper.error(message = bodyAsText())
                        }
                    )
                }
            } catch (e : TimeoutException) {
                callback(
                    ResponseWrapper<Any>(ResponseStatus.ERROR, message = "Request Time Out")
                )
            } catch (e: Exception) {
                callback(
                    ResponseWrapper<Any>(ResponseStatus.ERROR, message = e.message)
                )
            }
        }
    }

    suspend fun post(appendedPath: List<String>, body: Any?): HttpResponse {
        return ClientModule.instance.client.post {
            url.appendEncodedPathSegments(appendedPath)
            setBody(body)
        }
    }

    suspend fun get(appendedPath: List<String>, body: MutableMap<String, String>?): HttpResponse {
        return ClientModule.instance.client.get {
            url.apply {
                appendEncodedPathSegments(appendedPath)
                body?.forEach {
                    parameters.append(it.key, it.value)
                }
            }
        }
    }
}