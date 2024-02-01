package http.base

import com.google.gson.Gson
import http.base.wrapper.ResponseStatus
import http.base.wrapper.ResponseWrapper
import http.forgot.model.response.ResetPasswordResponse
import http.login.model.response.NormalLoginError
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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
                        when (status.value) {
                            in 200..299 -> ResponseWrapper.success(bodyAsText())
                            else -> ResponseWrapper.error(data = bodyAsText(), message = "")
                        }
                    )
                }
            } catch (e : TimeoutException) {
                callback(
                    ResponseWrapper<Any>(ResponseStatus.ERROR, message = "Request Timeout")
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

    suspend fun get(token: String? = "", appendedPath: List<String>, body: MutableMap<String, String>?): HttpResponse {
        return ClientModule.instance.client.get {
            url.apply {
                header(HttpHeaders.Authorization, "Bearer $token")
                appendEncodedPathSegments(appendedPath)
                body?.forEach {
                    parameters.append(it.key, it.value)
                }
            }
        }
    }

    fun process(it: ResponseWrapper<*>, success: Class<*>, failed: Class<*>, onFinished: (Boolean, String?, Class<*>?) -> Unit) {
        when (it.status) {
            ResponseStatus.SUCCESS -> {
                if (it.data == null)
                    onFinished(true, "No data returned, weird", null)
                else
                    Gson().fromJson(it.data.toString(), success::class.java).apply {
                        onFinished(false, null, this)
                    }
            }
            else -> {
                if (it.data == null) {
                    onFinished(true, it.message, null)
                    return
                }
                Gson().fromJson(it.data.toString(), failed::class.java).apply {
                    onFinished(true, null, this)
                }
            }
        }
    }

    inline fun <reified T> convert(originalClass: Class<*>): Class<T>? {
        // Check if the originalClass is assignable to the desired type
        if (T::class.java.isAssignableFrom(originalClass)) {
            @Suppress("UNCHECKED_CAST")
            return originalClass as Class<T>
        } else {
            // Handle the case where the types are not compatible
            return null
        }
    }
}