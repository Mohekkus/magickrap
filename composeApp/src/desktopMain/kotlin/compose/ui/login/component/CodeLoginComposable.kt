package compose.ui.login.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import appStorage
import compose.ui.reusable.minimalDialog
import http.ApiHandler
import http.base.ClientModule
import storage.directories.UserStorage

@Composable
fun codeLoginComposable(onLogged: () -> Unit) {
    var result by remember {
        mutableStateOf("")
    }
    var warning by remember {
        mutableStateOf("")
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
                        warning = "Code cannot be blank"
                        return@Button
                    }
                    code.length < 6 -> {
                        warning = "Code is less than required"
                        return@Button
                    }
                }

                ApiHandler.authentication.login(code,
                    onSuccess = {
                        it.data?.accessToken?.apply {
                            ClientModule.instance.bearerToken = toString()
                            appStorage.logged(toString())
                        }
                        result = "Login Succeeded"
                    },
                    onFailure = {
                        warning = it
                    })
            }
        ) {
            Text("Submit Code")
        }

        Text(warning)

        if (result.isNotEmpty()) {
            LocalClipboardManager.current.setText(
                AnnotatedString(result)
            )

            minimalDialog(result) {
                if (result == "Login Succeeded")
                    onLogged.invoke()

                result = ""
            }
        }
    }
}