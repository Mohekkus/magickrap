package compose.ui.certificate.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
    callback: (ServerCertificateItem) -> Unit
) {
    var server by remember {
        mutableStateOf(appStorage.servers())
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

            Column(modifier = Modifier
                .wrapContentSize()
                .verticalScroll(rememberScrollState())
            ) {
                server?.apply {
                    appStorage.servers(this)

                    if (this.isNotEmpty())
                        forEach { data ->
                            TextButton(
                                onClick = {
                                    callback(data)
                                }) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        serverCard(
                                            data.iconUrl,
                                            data.name
                                        )
                                    }

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


                                }
                            }

                        }
                }
            }
        }

    if (server == null)
        getServerCall {
            server = it.data?.items
        }

}