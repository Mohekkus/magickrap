package compose.ui.certificate.component

import CertificateDocument
import GeneratedCertificateResponse
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import appStorage
import compose.ui.reusable.componentCard
import compose.ui.reusable.serverCard
import etc.Global.toMutableMap
import http.ApiHandler
import http.certificate.implementation.CertificateRequests
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.response.CertificateResponse
import http.certificate.model.response.ServerCertificateResponse
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.ServerCertificateItem


fun getCertificateCall(
    servID: String,
    onFailure: (String) -> Unit,
    onSuccess: (CertificateResponse) -> Unit
) {
    val payload = CertificatePayload(
        filterserverId = servID
    )

    ApiHandler.certificate.getCertificate(payload,
        { onFailure(it) },
        { onSuccess(it) }
    )
}

fun generateCertificateCall(
    servID: String,
    onFailure: (String) -> Unit,
    onSuccess: (GeneratedCertificateResponse) -> Unit
) {
    ApiHandler.certificate.generateCertificate("wireguard", servID,
        { onFailure(it) }, { onSuccess(it) }
    )
}

@Preview
@Composable
fun getCertificateComponent(
    savedServer: ServerCertificateItem,
    savingCertificate: (CertificateDocument?) -> Unit
) {
    val protocol = appStorage.protocol()

    var certificate by remember {
        mutableStateOf<CertificateResponse?>(null)
    }

    var status by remember {
        mutableStateOf("Get")
    }

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            "$status Certificate",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight(800)
        )

        if (certificate != null)
            Column(modifier = Modifier
                .wrapContentSize()
                .verticalScroll(rememberScrollState())
            ) {
                certificate?.data?.items?.filter { it?.protocols == protocol.name }?.forEach {
                    componentCard {
                        TextButton(onClick = {
                            savingCertificate(it?.document)
                        }) {
                            Column {
                                Text(it?.server?.name.toString())
                                Text("${it?.protocols.toString()} protocol\n\n")
                                Text(it?.document?.client?.toMutableMap().toString())
                            }
                        }
                    }
                }
            }
    }

    if (certificate == null) {
        getCertificateCall(
            savedServer.id ?: "",
            onFailure = {
                status = "Generate"
            },
            onSuccess = {
                status = "List of"
                certificate = it
            }
        )
    }

    if (status == "Generate")
        generateCertificateCall(
            savedServer.id ?: "",
            onFailure = {
            },
            onSuccess = {
                status = "List of"
            }
        )
}