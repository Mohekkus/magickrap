package compose.ui.certificate

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import appStorage
import compose.ui.certificate.component.getCertificateComponent
import compose.ui.certificate.component.serverCertificateComponent
import compose.ui.reusable.minimalDialog
import http.ApiHandler
import http.base.ClientModule
import kotlin.math.log

class CertificateComposable {

    @Preview
    @Composable
    fun main(callback: () -> Unit) {
        MaterialTheme {
            var savedServer by remember {
                mutableStateOf(appStorage.saved())
            }
            var certificate by remember {
                mutableStateOf(appStorage.document())
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.weight(.9f)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        serverCertificateComponent {
                            savedServer = it
                        }
                    }
                    Box(modifier = Modifier.weight(1f)){
                        savedServer?.let {
                            getCertificateComponent(it) {
                                certificate = it
                            }
                        }
//                        CertificateRoute.CERTIFICATE.get().invoke()
                    }
                }

                var logoutMessages by remember {
                    mutableStateOf<String?>(null)
                }

                if (logoutMessages?.isNotEmpty() == true)
                    minimalDialog(
                        logoutMessages.toString()
                    ) {
                        logoutMessages = null
                    }

                Box {
                    Button(
                        onClick = {
                            callback.invoke()
//                            ApiHandler.logout.logout(
//                                token = ClientModule.instance.bearerToken ?: return@Button,
//                                onFailure = {
//                                    logoutMessages = it
//                                },
//                                onSuccess = {
//                                    callback.invoke()
//                                }
//                            )
                        }
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    }
}