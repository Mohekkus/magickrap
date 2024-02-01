package compose.ui.login.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import compose.ui.reusable.minimalDialog
import etc.Global
import http.ApiHandler

@Composable
fun normalLoginComposable() {
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
                        result = it.data?.accessToken ?: "You made a call, it succeeded but no token! Amazed"
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
            LocalClipboardManager.current.setText(
                AnnotatedString(result)
            )

            minimalDialog(result) {
                result = ""
            }
        }
    }
}

fun inputHandler(username: String, password: String): String? {
    if (username.isBlank() || password.isBlank())
        return "Username and Password cannot be nulled"


    if (!Global.emailRegex.matcher(username).matches())
        return "Doesn't seems like a correct email format"

    if (password.length < 6)
        return "Password cannot less than 6 characters"

    return null
}