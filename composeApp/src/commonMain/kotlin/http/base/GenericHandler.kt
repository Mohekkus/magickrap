package http.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import http.login.model.response.NormalLoginError
import http.login.model.response.NormalLoginResponse
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.TypeVariable
import kotlin.reflect.KType

object GenericHandler {
    fun runner(requests: suspend () -> HttpResponse, onComplete: (Pair<String, Boolean>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onComplete(
                requests().validation()
            )
        }
    }

    suspend fun post(appendedPath: List<String>, body: Any?): HttpResponse {
        return KtorClient.instance.post {
            url.appendEncodedPathSegments(appendedPath)
            setBody(body)
        }
    }

    private suspend fun HttpResponse.validation() =
        if (status.value in 200..299)
            Pair(bodyAsText(), true)
        else
            Pair(bodyAsText(), false)
}