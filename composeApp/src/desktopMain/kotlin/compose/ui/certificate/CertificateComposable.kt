package compose.ui.certificate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import appStorage
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import compose.ui.certificate.component.*
import compose.ui.reusable.minimalDialog
import desktopConfig
import http.base.AdditionalClient
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.kamel.image.config.LocalKamelConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vpn.VpnRunner


class CertificateComposable {

    @Preview
    @Composable
    fun main(callback: () -> Unit) {
        var painter by remember {
            mutableStateOf<Resource<Painter>?>(null)
        }

        CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
            painter = asyncPainterResource("logo/Auxonode.svg")
        }

        var expandingServer by remember {
            mutableStateOf(false)
        }
        var expandingProtocol by remember {
            mutableStateOf(false)
        }
        var expandingCertificate by remember {
            mutableStateOf(false)
        }

        var selectedServer by remember {
            mutableStateOf(appStorage.saved())
        }
        var selectedProtocol by remember {
            mutableStateOf(appStorage.protocol())
        }
        var selecterCertificate by remember {
            mutableStateOf(appStorage.document())
        }

        if (selectedProtocol != appStorage.protocol())
            appStorage.protocol(selectedProtocol.key())

        MaterialTheme {
            Row {
                Column(
                    modifier = Modifier.width(46.dp),
                ) {
                    if (painter != null)
                        Card(
                            modifier = Modifier.wrapContentWidth().fillMaxWidth(),
                            shape = RoundedCornerShape(
                                bottomEnd = 8.dp
                            )
                        ) {
                            painter?.let {
                                when (it) {
                                    is Resource.Loading -> {
                                        Text("Loading...")
                                    }
                                    is Resource.Success -> {
                                        Image(
                                            modifier = Modifier
                                                .padding(4.dp),
                                            painter = it.value,
                                            contentDescription = "Profile"
                                        )
                                    }
                                    is Resource.Failure -> {
                                        println(it.exception)
                                    }
                                }
                            }
                        }
                    Box(
                        modifier = Modifier
                            .weight(.1f)
                    ) {}

                    Card(
                        modifier = Modifier.wrapContentHeight(),
                        shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                    ) {
                        Column {
                            TextButton(
                                onClick = {
                                    expandingServer = !expandingServer
                                    expandingProtocol = false
                                    expandingCertificate = false
                                }
                            ) {
                                Icon(
                                    FeatherIcons.Server,
                                    modifier = Modifier.padding(6.dp),
                                    contentDescription = "Server"
                                )
                            }

                            TextButton(
                                onClick = {
                                    expandingServer = false
                                    expandingProtocol = !expandingProtocol
                                    expandingCertificate = false
                                }
                            ) {
                                Icon(
                                    FeatherIcons.Shield,
                                    modifier = Modifier.padding(7.dp),
                                    contentDescription = "VPN Protocol"
                                )
                            }

                            TextButton(
                                onClick = {
                                    expandingServer = false
                                    expandingProtocol = false
                                    expandingCertificate = !expandingCertificate
                                }
                            ) {
                                Icon(
                                    FeatherIcons.File,
                                    modifier = Modifier.padding(6.dp),
                                    contentDescription = "Certificate"
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {

                    }

                    Card(
                        modifier = Modifier.wrapContentSize(),
                        shape = RoundedCornerShape(topEnd = 8.dp)
                    ) {
                        TextButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                FeatherIcons.Settings,
                                modifier = Modifier.padding(6.dp),
                                contentDescription = "Server"
                            )
                        }
                    }
                }

                Box (
                    modifier = Modifier.wrapContentSize().padding(16.dp).weight(
                        if (expandingCertificate || expandingServer || expandingProtocol)
                            .7f
                        else .001f
                    )
                ) {
                    expandingComponent(
                        {
                            selectedServer.let { certificateItem ->
                                serverCertificateComponent(selectedServer) {
//                                        expandingServer = false
                                    selectedServer = it
                                }
                            }
                        }
                        , expandingServer
                    )

                    expandingComponent(
                        {
                            selectedServer?.let {
                                getCertificateComponent(it, selectedProtocol) {
//                                        expandingCertificate = false
                                }
                            }
                        },
                        expandingCertificate
                    )

                    expandingComponent(
                        {
                            getProtocolComponent(selectedProtocol) {
//                                expandingProtocol = false
                                selectedProtocol = it
                                appStorage.protocol(it)
                            }
                        },
                        expandingProtocol
                    )
                }

                var ipaddress by remember {
                    mutableStateOf("...")
                }

                if (ipaddress == "...")
                    getIpAddress {
                        if (it != null)
                            ipaddress = it
                    }

                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    var log by remember {
                        mutableStateOf("")
                    }
                    Card {
                        val uriHandlker = LocalUriHandler.current
                        Column (modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ipaddress)
                                TextButton(
                                    onClick = { uriHandlker.openUri("https://checkip.amazonaws.com") }
                                ) {
                                    Text("Check Manually")
                                }
                            }

                            Text(
                                """
                                To ensure the application preview the working code please select:
                                
                                Protocol into OpenVPN_UDP
                                Server into United States 1
                                
                                Note this app still not properly showing status connection so please be patient
                            """.trimIndent()
                            )

                            FloatingActionButton(
                                modifier = Modifier.height(32.dp),
                                onClick = {
                                    VpnRunner.instance.apply {
                                        if (status()) {
                                            terminate()
                                            return@FloatingActionButton
                                        }

                                        selecterCertificate?.let { document ->
                                            start(document) {
                                                log += "\n$it"

                                                if (it.contains("DISCONNECTED") || it.contains("CONNECTED"))
                                                    ipaddress = "..."
                                            }
                                        }

                                    }
                                }
                            ) {
                                Icon(
                                    FeatherIcons.Power,
                                    contentDescription = "Power On/Off",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    Text(
                        log,
                        modifier = Modifier.verticalScroll(
                            rememberScrollState()
                        )
                    )
                }
            }
        }
    }
}

fun getIpAddress(callback: (String?) -> Unit) {
    CoroutineScope(Dispatchers.Default).launch {
        try {
            AdditionalClient().client("checkip.amazonaws.com").get {}
                .apply {
                    callback(
                        bodyAsText()
                    )
                }
        } catch (e: Exception) {
            callback(e.message)
//            getIpAddress(callback)
        }
    }
}

@Composable
fun expandingComponent(child: @Composable () -> Unit, isExpanding: Boolean) {
    var expanding by remember {
        mutableStateOf(isExpanding)
    }

    LaunchedEffect(isExpanding) {
        expanding = isExpanding
    }

    AnimatedVisibility(
        expanding,
        enter = expandIn(
            // Overwrites the default spring animation with tween
            animationSpec = tween(300, easing = FastOutSlowInEasing),
        ) {
            // Overwrites the initial size to 50 pixels by 50 pixels
            IntSize(50, 50)
        },
        exit = shrinkOut(
            tween(300, easing = LinearOutSlowInEasing),
        ) { fullSize ->
            // Overwrites the target size of the shrinking animation.
            IntSize(fullSize.width / 10, fullSize.height / 5)
        }
    ) {
        child()
    }
}