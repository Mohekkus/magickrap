package compose.ui.certificate.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import appStorage
import compose.ui.reusable.componentCard
import storage.directories.ProtocolStorage

@Composable
fun getProtocolComponent(
    protocol: ProtocolStorage.PROTOCOL,
    callback: (ProtocolStorage.PROTOCOL) -> Unit
) {
    var tempProtocol by remember {
        mutableStateOf(appStorage.protocol())
    }

    LaunchedEffect(protocol) {
        tempProtocol = protocol
    }

    Column {
        Text(
            "Protocol ${tempProtocol.protocolText()}",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight(800),
            modifier = Modifier.padding(bottom = 16.dp)
        )


        componentCard(
            modifier = Modifier
                .padding(bottom = 18.dp)
        ) {
            Column {
                ProtocolStorage.PROTOCOL.WIREGUARD.apply {
                    protocolView(
                        this,
                        tempProtocol == this
                    ) {
                        callback(this)
                        tempProtocol = this
//                        protocolExpand = !protocolExpand
                    }
                }
                ProtocolStorage.PROTOCOL.OPENVPN_UDP.apply {
                    protocolView(
                        this,
                        tempProtocol == this
                    ) {
                        callback(this)
                        tempProtocol = this
//                        protocolExpand = !protocolExpand
                    }
                }
                ProtocolStorage.PROTOCOL.OPENVPN_TCP.apply {
                    protocolView(
                        this,
                        tempProtocol == this
                    ) {

//                        callback(this)
//                        protocolExpand = !protocolExpand
                    }
                }
            }
        }
    }
}