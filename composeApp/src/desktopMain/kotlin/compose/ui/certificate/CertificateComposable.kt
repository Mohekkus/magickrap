package compose.ui.certificate

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
import compose.ui.certificate.component.getCertificateComponent
import compose.ui.certificate.component.serverCertificateComponent
import compose.ui.reusable.minimalDialog
import http.certificate.model.response.ServerCertificateResponse

class CertificateComposable {

    @Preview
    @Composable
    fun main() {
        MaterialTheme {
            if (LocalClipboardManager.current.hasText() == false)
                minimalDialog("Seems like there is no token copied in clipboard") {

                }

            var savedServer by remember {
                mutableStateOf<ServerCertificateResponse.ServerCertificateData.ServerCertificateItem?>(null)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row {
                    Box(modifier = Modifier.weight(1f)) {
                        serverCertificateComponent {
                            savedServer = null
                            savedServer = it
                        }
                    }
                    Box(modifier = Modifier.weight(1f)){
                        savedServer?.let { getCertificateComponent(it) }
//                        CertificateRoute.CERTIFICATE.get().invoke()
                    }
                }

                Text("Log")
            }
        }
    }
}