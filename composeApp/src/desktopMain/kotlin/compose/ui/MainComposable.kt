package compose.ui

import GeneratedCertificateResponse
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import appStorage
import compose.MainRoute
import compose.icons.FeatherIcons
import compose.icons.SimpleIcons
import compose.icons.feathericons.Power
import compose.icons.feathericons.Settings
import compose.icons.feathericons.Shield
import compose.icons.feathericons.ShieldOff
import compose.icons.simpleicons.Openvpn
import compose.icons.simpleicons.Wireguard
import compose.ui.certificate.component.getProtocolComponent
import compose.ui.certificate.component.protocolText
import etc.NetworkUtility
import http.ApiHandler
import http.certificate.model.payload.CertificatePayload
import http.certificate.model.response.CertificateResponse
import logic.ConnectionLogic
import storage.directories.ProtocolStorage
import vpn.VpnRunner

class MainComposable {

    companion object {
        val instance = MainComposable()
    }

    fun getCertificateCall(
        servID: String,
        onFailure: (String) -> Unit,
        onSuccess: (CertificateResponse) -> Unit
    ) {
        val payload = CertificatePayload(
            filterserverId = servID,
            sort = ""
        )

        ApiHandler.certificate.getCertificate(
            payload,
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
    fun main(navigate: (MainRoute) -> Unit) {
        var status by remember {
            mutableStateOf("DISCONNECT")
        }

        var ipAddress by remember {
            mutableStateOf("...")
        }

        if (ipAddress == "...")
            NetworkUtility.getIpAddress {
                ipAddress = it ?: "..."
            }

        var selectedProtocol by remember {
            mutableStateOf(appStorage.protocol())
        }

        var selectedServer by remember {
            mutableStateOf(appStorage.picked())
        }

        var selectedCertificate by remember {
            mutableStateOf(appStorage.document())
        }

        var openSetting by remember {
            mutableStateOf(false)
        }

        if (openSetting)
            settingWindow {
                openSetting = false
            }

        getProtocolComponent(selectedProtocol) {
            selectedProtocol = it
            appStorage.protocol(it)
            status = "..."
        }

//        serverCertificateComponent(selectedServer) {
//            selectedServer = it
//            appStorage.save(it)
//            status = "..."
//        }

        if (status == "...")
            getCertificateCall(
                servID = selectedServer?.id ?: return,
                onSuccess = { response ->
                    response.data?.items?.filter { it?.server?.id == selectedServer?.id }?.firstOrNull{
                        it?.protocols == selectedProtocol.protocolText().lowercase()
                    }?.document?.let {
                        status = ""
                        selectedCertificate = it
                        appStorage.document(it)
                    } ?: run {
                        status = "Generate"
                    }
                },
                onFailure = {
                    println(it)
                    selectedCertificate = null
                }
            )

        if (status == "Generate")
            generateCertificateCall(
                selectedServer?.id ?: run {
                    status = "Selected Server is null"
                    return
                },
                onFailure = {
                    status = "Failed"
                },
                onSuccess = { response ->
                    status = "Generated"
                    response.data.let {
                        if (it == null) status = "Retrying"
                        else {
                            status = ""
                            selectedCertificate = it.document
                            appStorage.document(it.document)
                        }
                    }

                }
            )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Current IP Address")
            Text(ipAddress)

            FloatingActionButton(
                onClick =  {
                    if (status != "DISCONNECT") {
                        ConnectionLogic.instance.stop {
                            status = "DISCONNECTED"
                        }
                        return@FloatingActionButton
                    }

                    selectedCertificate?.let { document ->
                        VpnRunner.instance.start(document) {
                            status = it
                        }
                    } ?: run {
                        println("no certificate")
                        status = "..."
                    }
                },
                backgroundColor = Color.Green
            ) {
                Icon(
                    modifier = Modifier
                        .size(128.dp)
                        .padding(24.dp),
                    imageVector =  FeatherIcons.Power,
                    tint = Color.White,
                    contentDescription = "Toggle Connection Button"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (status == "CONNECTED")
                        FeatherIcons.Shield
                    else FeatherIcons.ShieldOff,
                    contentDescription = "Status"
                )

                Text(
                    if (status == "CONNECTED")
                        "You are protected"
                    else
                        "You are unprotected"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        Color.White
                    ),
                    onClick = {

                    }
                ) {
                    Row(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            when (selectedProtocol) {
                                ProtocolStorage.PROTOCOL.OPENVPN_UDP, ProtocolStorage.PROTOCOL.OPENVPN_TCP ->
                                    SimpleIcons.Openvpn
                                else ->
                                    SimpleIcons.Wireguard
                            },
                            contentDescription = "Protocol"
                        )
                        Box(modifier = Modifier.width(16.dp))

                        Text(selectedServer?.name.toString())
                    }
                }
                Box(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        openSetting = true
                    }
                ) {
                    Icon(
                        FeatherIcons.Settings,
                        contentDescription = "Setting"
                    )
                }
            }
        }
    }

    @Composable
    fun settingWindow(callback: () -> Unit) {
        Window(
            onCloseRequest = callback
        ) {
            Row {

            }
        }
    }
}

