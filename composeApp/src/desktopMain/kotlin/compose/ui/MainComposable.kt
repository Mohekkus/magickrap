package compose.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import compose.icons.AllIcons
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.User
import compose.icons.fontawesomeicons.solid.UserShield
import compose.ui.reusable.minimalDialog
import etc.NetworkUtility

class MainComposable {

    companion object {
        val instance = MainComposable()
    }

    @Preview
    @Composable
    fun main(logout: () -> Unit) {
        var ipAddress by remember {
            mutableStateOf("...")
        }

        if (ipAddress == "...")
            NetworkUtility.getIpAddress {
                ipAddress = it ?: "..."
            }

        Column {
            Text("Current IP Address")
            Text(ipAddress)

            FloatingActionButton(
                onClick =  {}
            ) {
                Icon(
                    modifier = Modifier
                        .size(128.dp)
                        .padding(24.dp),
                    imageVector = FontAwesomeIcons.Solid.UserShield,
                    contentDescription = "Toggle Connection Button"
                )
            }

            Text("This is VPN connection status placeholder")

            Row {
                Card {
                    Row {

                    }
                }
                Box(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {

                    }
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.User,
                        contentDescription = "Profile"
                    )
                }
            }
        }
    }
}

