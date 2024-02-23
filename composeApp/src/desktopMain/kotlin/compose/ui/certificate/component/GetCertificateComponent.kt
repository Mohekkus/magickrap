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
    savingCertificate: (CertificateDocument?) -> Unit
) {
    var lastServer by remember {
        mutableStateOf(appStorage.saved())
    }

    var protocol by remember {
        mutableStateOf(appStorage.protocol())
    }
    var protocolExpand by remember {
        mutableStateOf(false)
    }

    var certificate by remember {
        mutableStateOf(appStorage.document())
    }
    var certificateExpand by remember {
        mutableStateOf(false)
    }

    var status by remember {
        mutableStateOf("Get")
    }
    var generateFailed by remember {
        mutableStateOf<String?>(null)
    }


    if (savedServer != lastServer) {
        lastServer = savedServer
        status = "Get"
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(end = 24.dp),
    ) {
        Text(
            "Certificate",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight(800),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            onClick = {
                protocolExpand = !protocolExpand
            },
            contentPadding = PaddingValues(0.dp)
        ) {
            componentCard {
                Text(
                    "Protocol ${protocol.protocolText()}",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight(600),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (protocolExpand)
            componentCard(
                modifier = Modifier
                    .padding(bottom = 18.dp)
            ) {
                Column {
                    protocolView(
                        ProtocolStorage.PROTOCOL.WIREGUARD,
                        protocol == ProtocolStorage.PROTOCOL.WIREGUARD
                    ) {
                        ProtocolStorage.PROTOCOL.WIREGUARD.apply {
                            if (this != protocol) {
                                protocol = this
                                status = "Get"
                            }
                        }
                        protocolExpand = !protocolExpand
                    }
                    protocolView(
                        ProtocolStorage.PROTOCOL.OPENVPN_UDP,
                        protocol == ProtocolStorage.PROTOCOL.OPENVPN_UDP
                    ) {
                        ProtocolStorage.PROTOCOL.OPENVPN_UDP.apply {
                            if (this != protocol) {
                                protocol = this
                                status = "Get"
                            }
                        }
                        protocolExpand = !protocolExpand
                    }
                    protocolView(
                        ProtocolStorage.PROTOCOL.OPENVPN_TCP,
                        protocol == ProtocolStorage.PROTOCOL.OPENVPN_TCP
                    ) {
                        ProtocolStorage.PROTOCOL.OPENVPN_TCP.apply {
                            if (this != protocol) {
                                protocol = this
                                status = "Get"
                            }
                        }
                        protocolExpand = !protocolExpand
                    }
                }
            }

        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if (status == "")
                        certificateExpand = !certificateExpand
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                componentCard {
                    Text(
                        "$status Certificate",
                        modifier = Modifier.fillMaxSize(),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight(600)
                    )
                }
            }

            if (status == "" || status == "Failed")
                IconButton(
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(.2f)
                        .padding(start = 8.dp),
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
                        .weight(.2f)
                        .padding(start = 8.dp),
                    strokeWidth = 8.dp
                )
        }

        when (status) {
            "" ->
                when {
                    certificateExpand ->
                        componentCard(
                            modifier = Modifier.padding(bottom = 18.dp)
                        ) {
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
                        }
                    certificate != null ->
                        Button(
                            modifier = Modifier.fillMaxSize(),
                            onClick = {
                                appVpn.start(data = certificate ?: return@Button) {

                                }
                            },
                        ) {
                            Text("Execute")
                        }
                }
        }
    }

    when (status) {
        "Get", "Retrying" -> {
            appStorage.protocol(protocol.key())
            certificateExpand = false
            getCertificateCall(
                lastServer?.id ?: "",
                onFailure = {
                    status = "Generate"
                },
                onSuccess = { response ->
                    status = "Processing"
                    response.data?.items?.first { it?.protocols == protocol.key() }.let {
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