import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import etc.Global
import http.ApiHandler
import http.base.RequestsInterface
import http.login.model.response.CodeLoginError
import http.login.model.response.CodeLoginResponse
import http.login.model.response.NormalLoginError
import http.login.model.response.NormalLoginResponse
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {

    var text by remember { mutableStateOf("") }

    MaterialTheme {
        Column {
            Row(horizontalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier.wrapContentSize()
                        .padding(8.dp)
                ) {
                    Text("Login by Email + Password")

                    var username by remember {
                        mutableStateOf("")
                    }
                    var password by remember {
                        mutableStateOf("")
                    }

                    TextField(
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        singleLine = true
                    )
                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank()) {
                                text = "Username and Password cannot be nulled"
                                return@Button
                            }

                            if (!Global.emailRegex.matcher(username).matches()){
                                text = "Doesn't seems like a correct email format"
                                return@Button
                            }

                            if (password.length < 6) {
                                text = "Password cannot less than 6 characters"
                                return@Button
                            }

                            ApiHandler.authentication.login(username, password) {

                            }

                            CoroutineScope(Dispatchers.Default).launch {
                                val request = RequestsInterface.login.normalLogin(username, password)
                                request.let { response ->
                                    try {
                                        if (response.status.value in 200..299)
                                            Gson().fromJson(request.bodyAsText(), NormalLoginResponse::class.java).let { success ->
                                                text = success.data?.accessToken.toString()
                                            }
                                        else
                                            Gson().fromJson(request.bodyAsText(), NormalLoginError::class.java).let { failed ->
                                                failed.data?.forEach {
                                                    text += it
                                                }
                                            }
                                    } catch (e: Exception) {
                                        text = "Login Failed Generic"
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Submit Credential")
                    }
                }
                Column(
                    modifier = Modifier.wrapContentSize()
                        .padding(8.dp)
                ) {
                    var code by remember {
                        mutableStateOf("")
                    }

                    Text("Login by Code")

                    TextField(
                        value = code,
                        singleLine = true,
                        onValueChange = {
                            if (code.length < 6)
                                code = it
                        }
                    )

                    Button(
                        onClick = {
                            when {
                                code.isBlank() -> {
                                    text = "Code cannot be blank"
                                    return@Button
                                }
                                code.length < 6 -> {
                                    text = "Code is less than required"
                                    return@Button
                                }
                            }

                            CoroutineScope(Dispatchers.Default).launch {
                                RequestsInterface.login.codeLogin(code = code).let {
                                    try {
                                        if (it.status.value in 200..299)
                                            Gson().fromJson(it.bodyAsText(), CodeLoginResponse::class.java).let { success ->
                                                text = success.data?.accessToken.toString()
                                            }
                                        else
                                            Gson().fromJson(it.bodyAsText(), CodeLoginError::class.java).let { failed ->
                                                text = failed.meta?.message.toString()
                                            }
                                    } catch (e: Exception) {
                                        text = "Login Failed Generic"
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Submit Code")
                    }
                }
                Text(text)
            }
            Row {
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            RequestsInterface.login.qrLogin().let {
                                try {
                                    if (it.status.value in 200..299)
                                        Gson().fromJson(it.bodyAsText(), CodeLoginResponse::class.java).let { success ->
                                            text = success.data?.accessToken.toString()
                                        }
                                    else
                                        Gson().fromJson(it.bodyAsText(), CodeLoginError::class.java).let { failed ->
                                            text = failed.meta?.message.toString()
                                        }
                                } catch (e: Exception) {
                                    text = "Login Failed Generic"
                                }
                            }
                        }
                    }
                ) {
                    Text("QR")
                }
            }
        }
    }
}