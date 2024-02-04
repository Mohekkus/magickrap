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


val login = LoginComposable()
val certificate = CertificateComposable()

fun main() = application {
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
                    route = when (route) {
                        MainRoute.LOGIN -> {
                            MainRoute.CERTIFICATE
                        }

                        MainRoute.CERTIFICATE -> {
                            MainRoute.LOGIN
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