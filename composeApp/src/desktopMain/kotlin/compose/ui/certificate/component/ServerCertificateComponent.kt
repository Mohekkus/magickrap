package compose.ui.certificate.component

import CertificateDocument
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import appStorage
import compose.ui.reusable.serverCard
import http.ApiHandler
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.response.ServerCertificateResponse
import http.certificate.model.response.ServerCertificateResponse.ServerCertificateData.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import storage.directories.ProtocolStorage.PROTOCOL

@Composable
fun getServerCall(serverList: (ServerCertificateResponse) -> Unit) {
    ApiHandler.certificate.availableServer(
        AvailableServerPayload(),
        onFailure = {},
        onSuccess = {
            serverList(it)
        }
    )
}

@Preview
@Composable
fun serverCertificateComponent(
    savedServer: ServerCertificateItem?,
    callback: (ServerCertificateItem) -> Unit
) {
    var serverList by remember {
        mutableStateOf(appStorage.servers())
    }
    var localServer by remember {
        mutableStateOf(appStorage.saved())
    }

    LaunchedEffect(savedServer) {
        localServer = savedServer
    }

    if (serverList != null)
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
        ) {
            Text(
                "Server",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight(800),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .verticalScroll(rememberScrollState())
            ) {
                serverList?.apply {
                    appStorage.servers(this)

                    if (this.isNotEmpty())
                        forEach { data ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(
                                        bottom = 16.dp,
                                        end = 16.dp
                                    )
                            ) {
                                var isFavorited by remember {
                                    mutableStateOf(false)
                                }

                                appStorage.favorite().apply {
                                    if (this != null) {
                                        firstOrNull { it.id == data.id }.let {
                                            if (it != null) isFavorited = true
                                        }
                                    }

                                    IconToggleButton(
                                        modifier = Modifier.weight(.1f),
                                        checked = isFavorited,
                                        onCheckedChange = {
                                            isFavorited = it

                                            CoroutineScope(Dispatchers.IO).launch {
                                                appStorage.apply {
                                                    if (!it)
                                                        unfavorited(data)
                                                    else
                                                        favorite(data)
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            if (!isFavorited) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                                            contentDescription = "Favorite",
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .weight(1f)
                                ) {
                                    TextButton(
                                        contentPadding = PaddingValues(0.dp),
                                        onClick = {
                                            callback(data)
//                                            savedServer = data
                                        }
                                    ) {
                                        serverCard(
                                            data.iconUrl,
                                            data.name
                                        ) {
                                            if (savedServer?.id == data.id)
                                                Row(
                                                    modifier = Modifier.fillMaxSize(),
                                                    horizontalArrangement = Arrangement.End
                                                ) {
                                                    Icon(
                                                        Icons.Filled.CheckCircle,
                                                        contentDescription = "Selected Server"
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }

    if (serverList == null)
        getServerCall {
            serverList = it.data?.items
        }
}