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
import etc.Global
import http.ApiHandler
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

                            ApiHandler.authentication.nlogin(username, password)
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

                            ApiHandler.authentication.clogin(code) {

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
                        ApiHandler.certificate.generateCertificate("wireguard", "0001")
                    }
                ) {
                    Text("QR")
                }
            }
        }
    }
}