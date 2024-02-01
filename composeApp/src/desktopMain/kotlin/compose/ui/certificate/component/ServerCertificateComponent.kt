package compose.ui.certificate.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.ui.reusable.cartItem
import http.ApiHandler
import http.certificate.model.response.ServerCertificateResponse


@Composable
fun getServerCall(serverList: (ServerCertificateResponse) -> Unit) {
    val token = LocalClipboardManager.current.getText().toString()
    ApiHandler.certificate.availableServer(token) { isFailed, message, data ->
        if(!isFailed && data != null)
            serverList(data)
    }
}

@Preview
@Composable
fun serverCertificateComponent(
    callback: (ServerCertificateResponse.ServerCertificateData.ServerCertificateItem) -> Unit
) {
    var server by remember {
        mutableStateOf<ServerCertificateResponse?>(null)
    }

    if (server != null)
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                "Server",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight(800)
            )

            if (server != null)
                Column(modifier = Modifier
                    .wrapContentSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    server?.data?.items?.forEach { data ->
                        TextButton(onClick = {
                            if (data != null) {
                                callback(data)
                            }
                        }) {
                            cartItem(
                                data?.iconUrl,
                                data?.name
                            )
                        }
                    }
                }
        }

    if (server == null) {
        getServerCall {
            server = it
        }
    }
}