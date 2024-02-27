package compose.ui.certificate.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        mutableStateOf(protocol)
    }
    var protocolExpand by remember {
        mutableStateOf(true)
    }

    Column {
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


        componentCard(
            modifier = Modifier
                .padding(bottom = 18.dp)
        ) {
            Column {
                ProtocolStorage.PROTOCOL.WIREGUARD.apply {
                    protocolView(
                        this,
                        protocol == this
                    ) {
                        callback(protocol)
//                        protocolExpand = !protocolExpand
                    }
                }
                ProtocolStorage.PROTOCOL.OPENVPN_UDP.apply {
                    protocolView(
                        this,
                        protocol == this
                    ) {
                        callback(protocol)
                        appStorage.protocol(this)
                        tempProtocol = this
//                        protocolExpand = !protocolExpand
                    }
                }
                ProtocolStorage.PROTOCOL.OPENVPN_TCP.apply {
                    protocolView(
                        this,
                        protocol == this
                    ) {
                        callback(protocol)
//                        protocolExpand = !protocolExpand
                    }
                }
            }
        }
    }
}