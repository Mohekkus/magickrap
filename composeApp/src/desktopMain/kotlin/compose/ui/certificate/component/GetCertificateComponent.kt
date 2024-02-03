package compose.ui.certificate.component

import GeneratedCertificateResponse
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import etc.Global.toMutableMap
import http.ApiHandler
import http.certificate.implementation.CertificateRequests
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.response.CertificateResponse
import http.certificate.model.response.ServerCertificateResponse


@Composable
fun getCertificateCall(
    servID: String,
    onFailure: (String) -> Unit,
    onSuccess: (CertificateResponse) -> Unit
) {
    val token = LocalClipboardManager.current.getText().toString()
    val payload = CertificatePayload(
        filterserverId = servID
    )

    ApiHandler.certificate.getCertificate(token, payload,
        { onFailure(it) },
        { onSuccess(it) }
    )
}

@Composable
fun generateCertificateCall(
    servID: String,
    onFailure: (String) -> Unit,
    onSuccess: (GeneratedCertificateResponse) -> Unit
) {
    val token = LocalClipboardManager.current.getText().toString()
    ApiHandler.certificate.generateCertificate(token, "wireguard", servID,
        { onFailure(it) }, { onSuccess(it) }
    )
}

@Preview
@Composable
fun getCertificateComponent(savedServer: ServerCertificateResponse.ServerCertificateData.ServerCertificateItem) {
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
                certificate?.data?.items?.forEach {
                    Card(modifier = Modifier.padding(16.dp)) {
                        Text(
                            it?.document?.toMutableMap().toString()
                        )
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
                status = ""
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
                status = ""
                // TODO
            }
        )
}