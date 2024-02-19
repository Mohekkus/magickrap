package compose.ui.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.ui.login.component.codeLoginComposable
import compose.ui.login.component.normalLoginComposable
import compose.ui.login.component.qrLoginComposable

class LoginComposable {

    @Composable
    fun main(callback: () -> Unit) {
        var current by remember {
            mutableStateOf(
                LoginRoutes.NORMAL
            )
        }
        var logged by remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                current.get {
                    callback.invoke()
                }.invoke()

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (current != LoginRoutes.NORMAL)
                        TextButton(
                            onClick = {
                                current = LoginRoutes.NORMAL
                            }
                        ) {
                            Text(
                                text ="by Username and Password",
                            )
                        }

                    if (current != LoginRoutes.CODE)
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                if (current == LoginRoutes.NORMAL)
                                    Color.Unspecified
                                else
                                    Color.Transparent
                            ),
                            onClick = {
                                current = LoginRoutes.CODE
                            }
                        ) {
                            Text(
                                text = "by Code",
                            )
                        }

                    if (current != LoginRoutes.QR)
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                if (current == LoginRoutes.NORMAL)
                                    Color.Unspecified
                                else
                                    Color.Transparent
                            ),
                            onClick = {
                                current = LoginRoutes.QR
                            }
                        ) {
                            Text(
                                text = "by QRCode",
                            )
                        }
                }
            }

        }
    }

    enum class LoginRoutes {
        NORMAL {
            @Composable
            override fun get(onLogged: () -> Unit): @Composable () -> Unit = {
                normalLoginComposable{
                    onLogged()
                }
            }
        },
        CODE {
            @Composable
            override fun get(onLogged: () -> Unit): @Composable () -> Unit = {
                codeLoginComposable { onLogged() }
            }
        },
        QR {
            @Composable
            override fun get(onLogged: () -> Unit): @Composable () -> Unit = {
                qrLoginComposable { onLogged() }
            }
        },
        SSO {
            @Composable
            override fun get(onLogged: () -> Unit): @Composable () -> Unit = {
                normalLoginComposable {
                    onLogged()
                }
            }
        };

        @Composable
        abstract fun get(onLogged: () -> Unit): @Composable () -> Unit
    }
}