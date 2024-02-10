package compose.ui.certificate

import CertificateDocument
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import appStorage
import compose.ui.certificate.component.getCertificateComponent
import compose.ui.certificate.component.serverCertificateComponent
import compose.ui.reusable.minimalDialog
import http.certificate.model.response.ServerCertificateResponse
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.ServerCertificateItem

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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row {
                    Box(modifier = Modifier.weight(1f)) {
                        serverCertificateComponent {
                            appStorage.save(it)
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
            }
        }
    }
}