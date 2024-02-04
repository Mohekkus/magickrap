import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.ui.certificate.CertificateComposable
import compose.ui.login.LoginComposable
import http.base.ClientModule
import storage.QuickStorage
import storage.QuickStorage.load


val login = LoginComposable()
val certificate = CertificateComposable()

fun getToken() {
    if (QuickStorage.has("token"))
        "token".load { token: String? ->
            if (token?.isEmpty() == false)
                ClientModule.instance.bearerToken = token
        }
}

fun main() = application {
    getToken()

    var route by remember {
        mutableStateOf(
            MainRoute.LOGIN
        )
    }

    Window(onCloseRequest = ::exitApplication, title = "desktop") {
        Column {
            TextButton(
                modifier = Modifier.wrapContentWidth(),
                onClick = {
                    when (route) {
                        MainRoute.LOGIN -> {
                            route = MainRoute.CERTIFICATE
                        }
                        MainRoute.CERTIFICATE -> {
                            route = MainRoute.LOGIN
                        }
                    }
                }
            ) {
                Text("Certificate")
            }

            Box(modifier = Modifier.weight(1f)) {
                route.get().invoke()
            }
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    MainRoute.LOGIN.get()
}