package compose.ui.certificate.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import appStorage
import compose.ui.reusable.cartItem
import http.ApiHandler
import http.certificate.model.payload.AvailableServerPayload
import http.certificate.model.response.ServerCertificateResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import storage.Storage

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
    callback: (ServerCertificateResponse.ServerCertificateData.ServerCertificateItem) -> Unit
) {
    var server by remember {
        mutableStateOf<ServerCertificateResponse?>(null)
    }
//    appStorage.purge()

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
                        TextButton(
                            onClick = {
                                if (data != null) {
                                    callback(data)
                                }
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    cartItem(
                                        data?.iconUrl,
                                        data?.name
                                    )
                                }

                                var isFavorited by remember {
                                    mutableStateOf(false)
                                }

                                var isChecking by remember {
                                    mutableStateOf(false)
                                }

                                appStorage.favorite().apply {
                                    if (this != null)
                                        firstOrNull { it.id == data?.id }.let {
                                            if (it != null) isFavorited = true
                                        }

                                    println(isFavorited)
                                    IconToggleButton(
                                        checked = isFavorited,
                                        onCheckedChange = {
                                            isFavorited = it
//                                            isChecking = true

                                            CoroutineScope(Dispatchers.IO).launch {
                                                appStorage.apply {
//                                                    purge()
                                                    if (!it)
//                                                        println("unfaved")
                                                        unfavorited(data!!)
                                                    else
//                                                        println("faved")
                                                        favorite(data!!)
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

    if (server == null) {
        getServerCall {
            server = it
        }
    }
}