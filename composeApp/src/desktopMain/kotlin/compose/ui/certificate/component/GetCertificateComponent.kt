package compose.ui.certificate.component

import CertificateDocument
import GeneratedCertificateResponse
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import appStorage
import appVpn
import compose.ui.reusable.componentCard
import compose.ui.reusable.minimalDialog
import http.ApiHandler
import http.base.ClientModule
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.response.CertificateResponse
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.ServerCertificateItem
import storage.directories.ProtocolStorage


fun getCertificateCall(
    servID: String,
    onFailure: (String) -> Unit,
    onSuccess: (CertificateResponse) -> Unit
) {
    val payload = CertificatePayload(
        filterserverId = servID,
        sort = ""
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
    ApiHandler.certificate.generateCertificate(appStorage.protocol().key(), servID,
        { onFailure(it) }, { onSuccess(it) }
    )
}

@Preview
@Composable
fun getCertificateComponent(
    savedServer: ServerCertificateItem,
    savedProtocol: ProtocolStorage.PROTOCOL,
    savingCertificate: (CertificateDocument?) -> Unit
) {
    var lastServer by remember {
        mutableStateOf(appStorage.saved())
    }
    var protocol by remember {
        mutableStateOf(appStorage.protocol())
    }

    var certificate by remember {
        mutableStateOf(appStorage.document())
    }

    var status by remember {
        mutableStateOf(
            if (certificate == null) "Get" else ""
        )
    }
    var generateFailed by remember {
        mutableStateOf<String?>(null)
    }

    if (savedServer != lastServer) {
        lastServer = savedServer
        status = "Get"
    }

    if (savedProtocol != protocol) {
        protocol = savedProtocol
        status = "Get"
    }

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "$status Certificate",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight(800),
                modifier = Modifier.padding(bottom = 16.dp)
                    .weight(.85f)
            )

            if (status == "" || status == "Failed")
                IconButton(
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(.2f)
                        .padding(bottom = 16.dp),
                    onClick = {
                        status = "Get"
                        certificate = null
                    }
                ) {
                    Icon(
                        modifier = Modifier.wrapContentSize(),
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Refresh Certificate",
                    )
                }
            else
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(.2f),
                    strokeWidth = 4.dp
                )
        }

        componentCard(
            modifier = Modifier.padding(bottom = 18.dp),
        ) {
            if (status == "")
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Server: ${savedServer.hostName}",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        "Protocol: ${protocol.protocolText()}",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        "Certificate id: ${certificate?.client?.id.toString()}",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            else
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
        }
    }

    when (status) {
        "Get", "Retrying" -> {
            appStorage.protocol(protocol.key())
            getCertificateCall(
                lastServer?.id ?: "",
                onFailure = {
                    status = "Generate"
                },
                onSuccess = { response ->
                    status = "Processing"
                    response.data?.items?.filter { it?.server?.id == lastServer?.id }?.first { it?.protocols == protocol.key() }.let {
                        if (it == null) status = "Generate"
                        else {
                            status = ""
                            certificate = it.document
                            appStorage.document(it.document)
                        }
                    }
                }
            )
        }
        "Generate" ->
            generateCertificateCall(
                savedServer.id ?: "",
                onFailure = {
                    generateFailed = it
                    status = "Failed"
                },
                onSuccess = { response ->
                    status = "Generated"
                    response.data.let {
                        if (it == null) status = "Retrying"
                        else {
                            status = ""
                            certificate = it.document
                        }
                    }

                }
            )
        "Failed" ->
            if (generateFailed != null)
                minimalDialog(
                    generateFailed.toString()
                ) {
                    generateFailed = null
                }
    }

}

@Composable
fun protocolView(protocol: ProtocolStorage.PROTOCOL, isEnabled: Boolean = false, onClick: () -> Unit) {
    TextButton(onClick = { onClick.invoke() }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.weight(1f),
                color = Color.Black,
                text = protocol.protocolText()
            )
            Switch(
                checked = isEnabled,
                enabled = !isEnabled,
                onCheckedChange = {
                    onClick.invoke()
                }
            )
        }
    }
}

fun ProtocolStorage.PROTOCOL.protocolText(): String {
    val protocolString = this.name.lowercase()
    var name: String
    protocolString.apply {
        split("_").let {
            name = it.first().lowercase().replaceFirstChar { it.uppercase() } + " " + if (it.size > 1) it.last().uppercase() else ""
        }
    }
    return name
}