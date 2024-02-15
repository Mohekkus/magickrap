package compose.ui.login.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import appStorage
import compose.ui.reusable.minimalDialog
import etc.Global
import http.ApiHandler
import http.base.ClientModule
import http.base.response.ErrorMessages
import storage.QuickStorage.save
import storage.directories.UserStorage

@Composable
fun normalLoginComposable(onLogged: () -> Unit) {
    var warning by remember {
        mutableStateOf("")
    }
    var result by remember {
        mutableStateOf("")
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.wrapContentSize()
    ) {
        Text("Login by Email + Password")

        var username by remember {
            mutableStateOf("mohekkuslocker@gmail.com")
        }
        var password by remember {
            mutableStateOf("Mohekkus1!")
        }

        TextField(
            value = username,
            onValueChange = {
                if (warning.isNotEmpty())
                    warning = ""
                username = it
            },
            singleLine = true,
            modifier = Modifier
                .width(320.dp)
                .padding(8.dp)
        )
        TextField(
            value = password,
            onValueChange = {
                if (warning.isNotEmpty())
                    warning = ""
                password = it
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.wrapContentSize()
                .width(320.dp)
                .padding(8.dp)
        )

        Button(
            onClick = {
                warning = inputHandler(username, password) ?: ""

                if (warning.isNotEmpty()) return@Button

                ApiHandler.authentication.login(username, password,
                    onSuccess = {
                        if (it.data?.accessToken?.isEmpty() == true)
                            warning = ErrorMessages.SUCCESS_NO_DATA.value()
                        else {
                            it.data?.accessToken?.apply {
                                ClientModule.instance.bearerToken = toString()
                                appStorage.logged(toString())
                            }
                            result = "Login Succeeded"
                        }
                    },
                    onFailure = {
                        warning = it
                    }
                )
            }
        ) {
            Text("Submit Credential")
        }

        Text(warning)

        if (result.isNotEmpty()) {
            minimalDialog(result) {
                if (result == "Login Succeeded")
                    onLogged.invoke()

                result = ""
            }
        }
    }
}

fun inputHandler(username: String, password: String): String? {
    if (username.isBlank() || password.isBlank())
        return "Username and Password cannot be nullified"

    if (!Global.emailRegex.matcher(username).matches())
        return "Doesn't seems like a correct email format"

    if (password.length < 6)
        return "Password cannot less than 6 characters"

    return null
}