package compose.ui.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import compose.ui.reusable.minimalDialog
import etc.DeviceInfo
import http.ApiHandler
import http.login.model.request.QRLoginPayload
import http.login.model.response.QRLoginCreatedResponse
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.StringJoiner
import java.util.Timer
import java.util.TimerTask
import kotlin.time.Duration.Companion.seconds

fun apiCall(update: (QRLoginCreatedResponse) -> Unit) {
    ApiHandler.authentication.login(
        QRLoginPayload(),
        onSuccess = {
            update(it)
        },
        onFailure = {
            //TODO
        }
    )
}

@Composable
fun qrLoginComposable() {
    var data by remember {
        mutableStateOf<QRLoginCreatedResponse?>(null)
    }

    var result by remember {
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.wrapContentSize(),
    ) {
        if (data == null)
            CircularProgressIndicator()
        else {
            DeviceInfo.instance.apply {
                qrImage(data?.data?.encrypted ?: "")

                if (data?.data?.timeout != null) {
                    var timeleft by remember {
                        mutableStateOf(
                            (System.currentTimeMillis() / 1000) -
                                    data?.data?.timeout!!
                        )
                    }

                    LaunchedEffect(Unit) {
                        while (timeleft >= 1) {
                            delay(1.seconds)
                            timeleft--
                        }

                        data = null
                    }

                    progressTimer(
                        timeleft,
                        (System.currentTimeMillis() / 1000) -
                                data?.data?.timeout!!
                    )
                }
            }
        }
    }

    if (data == null)
        apiCall {
            data = it
        }

    if (result.isNotEmpty()) {
        LocalClipboardManager.current.setText(
            AnnotatedString(result)
        )

        minimalDialog(result) {
            result = ""
        }
    }
}

@Composable
fun qrImage(encrypted: String) {
    DeviceInfo.instance.apply {
        val result = StringJoiner("@")
            .add(encrypted)
            .add(publicIP)
            .add(macAddress)
            .add(country)
            .add(devModel)
            .add(os)
            .toString()

        Image(
            rememberQrCodePainter(result),
            "QR code login"
        )
    }
}

@Composable
fun progressTimer(time: Long, timeout: Long) {
    val progress = (1 - (time.toDouble() / timeout.toDouble()))

    LinearProgressIndicator(
        progress = progress.toFloat(),
        modifier = Modifier
            .padding(16.dp)
    )

    Text(time.toString())
}