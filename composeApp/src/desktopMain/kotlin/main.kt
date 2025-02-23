import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import compose.MainRoute
import compose.ui.MainComposable
import compose.ui.SettingComposable
import compose.ui.certificate.CertificateComposable
import compose.ui.login.LoginComposable
import http.base.ClientModule
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.batikSvgDecoder
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.svgDecoder
import storage.Storage
import vpn.VpnRunner


val appStorage: Storage = Storage.instance
val appVpn = VpnRunner.instance

val primary = Color(0xFF0179fa)

val login = LoginComposable()
val certificate = CertificateComposable()
val main = MainComposable.instance
val setting = SettingComposable.instance

val desktopConfig = KamelConfig {
    takeFrom(KamelConfig.Default)
    // Available only on Desktop.
    resourcesFetcher()
    // Available only on Desktop.
    // An alternative svg decoder
    batikSvgDecoder()
    // adds an SvgDecoder (SVG)
    svgDecoder()
}

fun main() = application {
    val windowsState = rememberWindowState().apply {
        size = DpSize(500.dp, 700.dp)
        position = WindowPosition(Alignment.BottomEnd)
    }

    var route by remember {
        mutableStateOf(
            MainRoute.LOGIN
        )
    }
    var update by remember {
        mutableStateOf(false)
    }

    if (update)
        route = when (route) {
            MainRoute.LOGIN ->
                MainRoute.MAIN

            else ->
                MainRoute.LOGIN
        }

    if (ClientModule.instance.bearerToken?.isNotEmpty() == true)
        route = MainRoute.MAIN

    Window(
        onCloseRequest = {

            exitApplication()
        },
        title = "Auxonode Desktop",
        resizable = false,
        state = windowsState
    ) {
        Column {
            Box(modifier = Modifier.weight(1f)) {
                route.get {
                    route = when (it) {
                        MainRoute.LOGIN -> MainRoute.LOGIN
                        MainRoute.MAIN -> MainRoute.MAIN
                        else -> route
                    }
//                    route = when (route) {
//                        MainRoute.LOGIN ->
//                            MainRoute.CERTIFICATE
//                        MainRoute.SETTING ->
//                            MainRoute.SETTING
//                        else -> {
//                            ClientModule.instance.bearerToken = null
//                            appStorage.logged("")
//                            MainRoute.LOGIN
//                        }
//                    }
                }.invoke()
            }
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    MainRoute.LOGIN.get {

    }
}